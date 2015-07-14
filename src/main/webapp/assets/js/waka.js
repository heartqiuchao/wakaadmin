var waka = (function($) {
	"use strict";

	var redirectUrlDiv = "waka_redirect_url", 
		extraDataDiv = "waka_extra_data", 
		internalDataDiv = "waka_internal_data", 
		preAjaxCallbackHandlers = [],
		internalDataHandlers = [];

	function addPreAjaxCallbackHandler(fn) {
		preAjaxCallbackHandlers.push(fn);
	}

	function addInternalDataHandler(fn) {
		internalDataHandlers.push(fn);
	}

	function runPreAjaxCallbackHandlers($data) {
		return runGenericHandlers($data, preAjaxCallbackHandlers);
	}

	function runInternalDataHandlers($data) {
		return runGenericHandlers($data, internalDataHandlers);
	}

	function runGenericHandlers($data, handlers) {
		for (var i = 0; i < handlers.length; i++) {
			if (!handlers[i]($data)) {
				return false;
			}
		}
		return true;
	}

	function defaultErrorHandler(data) {
		// if (data.getAllResponseHeaders()) {
		alert("发生了未知错误");
		// }
	}

	function getExtraData($data) {
		return extractData($data, extraDataDiv);
	}

	function getInternalData($dom) {
		var extractedData = extractData($dom, internalDataDiv);

		if (($dom instanceof jQuery)
				&& ($dom.attr('id') == internalDataDiv + "_container")) {
			$dom.unwrap();
		}

		return extractedData;
	}

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

	function getCsrfToken() {
		var csrfTokenInput = $('input[name="csrfToken"]');
		if (csrfTokenInput.length == 0) {
			return null;
		}

		return csrfTokenInput.val();
	}

	function get(options, callback) {
		if (options == null) {
			options = {};
		}
		options.type = 'GET';
		return waka.ajax(options, callback);
	}

	function post(options, callback) {
		if (options == null) {
			options = {};
		}
		options.type = 'POST';
		return waka.ajax(options, callback);
	}

	function ajax(options, callback) {
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
	
    function serializeObject($object) {
        var o = {};
        var a = $object.serializeArray();
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
    
    
    function addUrlParam(search, key, val){
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
    
	function redirectIfNecessary($dom) {
		if (!($dom instanceof $)) {
			return true; // 不中断执行
		}

		if ($dom.attr("id") == redirectUrlDiv) {
			var redirectUrl = $dom.text();
			if (redirectUrl != null && redirectUrl != "") {
				window.location = redirectUrl;
				return false; // 中断执行
			}
		}

		return true;
	}
    
    addPreAjaxCallbackHandler(function($data) {
        return waka.redirectIfNecessary($data);
    });

	return {
		addPreAjaxCallbackHandler : addPreAjaxCallbackHandler,
		addInternalDataHandler : addInternalDataHandler,
		redirectIfNecessary : redirectIfNecessary,
		getExtraData : getExtraData,
		get : get,
		post : post,
		ajax : ajax,
		defaultErrorHandler : defaultErrorHandler,
		serializeObject : serializeObject,
		addUrlParam : addUrlParam,
	}
})($)