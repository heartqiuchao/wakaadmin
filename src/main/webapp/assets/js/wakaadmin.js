window.wakaadmin = window.wakaadmin || (function($, undefined) {
	"use strict";

	var REDIRECT_URL_DIV_ID = "waka_redirect_url", 
		EXTRA_DATA_DIV_ID="waka_extra_data"，
		INTERNAL_DATA_DIV_ID = "waka_internal_data", 
		
		preAjaxCallbackHandlers = [],
		internalDataHandlers = [];

	/**
	 * @private
	 */
	function runPreAjaxCallbackHandlers($data) {
		return runHandlers($data, preAjaxCallbackHandlers);
	}
	
	/**
	 * @private
	 */
	function runInternalDataHandlers($data) {
		return runHandlers($data, internalDataHandlers);
	}

	/**
	 * @private
	 */
	function runHandlers($data, handlers) {
		for (var i = 0; i < handlers.length; i++) {
			if (!handlers[i]($data)) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * @private 读取$dom中‘#dataDivId’子元素的内容，并试图将其解析成json对象。
	 *          无论成功与否，子元素'#dataDivId'最后都会从$dom中移除。
	 */
	function extractData($dom, dataDivId) {
		if (!($dom instanceof jQuery)) {
			return null;
		}

		var extractedData = null;
		var $dataDiv = $dom.find('#' + dataDivId);
		if ($dataDiv.length > 0) {
			try {
				extractedData = $.parseJSON($dataDiv.text());
			} catch (e) {
				console.log("解析失败，数据不能转换成json对象: " + $dataDiv.text());
			}
			$dataDiv.remove();
		}
		return extractedData;
	}
	
	function getInternalData($dom) {
		var extractedData = extractData($dom, INTERNAL_DATA_DIV_ID);

		if (($dom instanceof jQuery) && ($dom.attr('id') == INTERNAL_DATA_DIV_ID + "_container")) {
			$dom.unwrap();
		}

		return extractedData;
	}

	function getCsrfToken() {
		var csrfTokenInput = $('input[name="csrfToken"]');
		if (csrfTokenInput.length == 0) {
			return null;
		}

		return csrfTokenInput.val();
	}
	
	var exports = {};
	
	exports.addPreAjaxCallbackHandler =	function (handler) {
		preAjaxCallbackHandlers.push(handler);
	}
	
	exports.addInternalDataHandler = 	function (handler) {
		internalDataHandlers.push(handler);
	}
	
	exports.getExtraData = 	function ($data) {
		return extractData($data, EXTRA_DATA_DIV_ID);
	}
	
	exports.get = function (options, callback) {
		if (options == null) {
			options = {};
		}
		
		options.type = 'GET';
		return wakaadmin.ajax(options, callback);
	}
	
	exports.post = function (options, callback) {
		if (options == null) {
			options = {};
		}
		
		options.type = 'POST';
		return wakaadmin.ajax(options, callback);
	}
	
	exports.ajax = function (options, callback) {
		if (options.type == null) {
			options.type = 'GET';
		}

		if (options.type.toUpperCase() == 'POST') {
			if (typeof options.data == 'string') {
				if (options.data.indexOf('csrfToken') < 0) {
					var csrfToken = getCsrfToken();
					if (csrfToken != null) {
						if (options.data.indexOf('=') > 0) {
							options.data += "&";
						}

						options.data += "csrfToken=" + csrfToken;
					}
				}
			} else if (typeof options.data == 'object') {
				if (options.data['csrfToken'] == null
						|| options.data['csrfToken'] == '') {
					var csrfToken = getCsrfToken();
					if (csrfToken != null) {
						options.data['csrfToken'] = csrfToken;
					}
				}
			} else if (!options.data) {
				var csrfToken = getCsrfToken();
				if (csrfToken) {
					options.data = {
						'csrfToken' : csrfToken
					}
				}
			}
		}

		options.success = function(data) {
			if (typeof data == "string" && !this.doParse) {
				data = $($.trim(data));
			}

			var internalData = getInternalData(data);
			if (internalData != null) {
				runInternalDataHandlers(internalData);
			}

			if (runPreAjaxCallbackHandlers(data)) {
				var extraData = getExtraData(data);
				callback(data, extraData);
			}
		};

		if (!options.error) {
			options.error = function(data) {
				waka.defaultErrorHandler(data);
			};
		}

		return $.ajax(options);
	}
	
	exports.serializeForm = function ($form) {
        var o = {};
        var a = $form.serializeArray();
        $.each(a, function() {
            if (o[this.name] !== undefined) {
                if (!o[this.name].push) {
                    o[this.name] = [o[this.name]];
                }
                o[this.name].push(this.value || '');
            } else {
                o[this.name] = this.value || '';
            }
        });
        return o;
    }
	
	exports.addUrlParam = 	function (search, key, val){
        var newParam = key + '=' + val, params = '?' + newParam;

        if (search) {
            params = search.replace(new RegExp('[\?]' + key + '[^&]*'), '?' + newParam);
            if (params == search) {
                params = search.replace(new RegExp('[\&]' + key + '[^&]*'), '&' + newParam);
                if ( (params == search) ) {
                    params += '&' + newParam;
                }
            }
        }

        return params;
    };
    
    exports.redirectIfNecessary = function ($dom) {
		if (!($dom instanceof jQuery)) {
			return true; // 不中断执行
		}

		if ($dom.attr("id") == REDIRECT_URL_DIV_ID) {
			var redirectUrl = $dom.text();
			if (redirectUrl != null && redirectUrl != "") {
				window.location = redirectUrl;
				return false; // 中断执行
			}
		}

		return true;
	}

	exports.defaultErrorHandler = function (data) {
		// if (data.getAllResponseHeaders()) {
		alert("发生了未知错误");
		// }
	}

    addPreAjaxCallbackHandler(function($data) {
        return wakaadmin.redirectIfNecessary($data);
    });

	return exports;
})($)