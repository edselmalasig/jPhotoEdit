/**
 * IIQ Publisher Exchange Bid version 28/02/2017
 */


// namespace
var IIQ = {}


// singleton
IIQ.PEB = (function() {
	
	/* private section */
	
	/**
	 * 
	 * @param start ,inclusive, must be a positive int 
	 * @param end ,inclusive, must be a positive int >= start
	 */
	var getRandom = function (start, end) {
		return Math.floor((Math.random() * (end-start+1)) + start);
	}


	/**
	 * Do not use as a replacement for (typeof val !== "undefined") - 
	 * works only for undefined function arguments and undefined properties
	 * @param val
	 * @returns {Boolean}
	 */
	var isDefined = function (val) {
		return (typeof val !== "undefined" && val != null);
	}
	
	
	var isNotEmptyStr = function (str) {
		return  isDefined(str) && str!="" && typeof str.length !== "undefined" && str.length > 0;
	}
	
	
	var getPageUrl = function() {
		var url = "";
		if (typeof window.location.href !== "undefined") {
			url = window.location.href;
			if (typeof window.location.href.toString !== "undefined") {
				url = window.location.href.toString();
			}
		}
		return url;
	}
	
	
	var getProtocol = function () {
		var protocol = "http:";
		var tmpProtocol = "";
		if (typeof window.location.protocol !== "undefined") {
			tmpProtocol = window.location.protocol;
		}
		else if (typeof document.location.protocol !== "undefined") {
			tmpProtocol = document.location.protocol;
		} 
		if (tmpProtocol == "https:") {
			protocol = "https:";
		}
		return protocol;
	}
	
	
	var getExceptionStr = function (e) {
		return " Name : " + e.name + "\n Message : " + e.message +
		(isDefined(e.description) ? "\n Description : " + e.description: "") +
		(isDefined(e.number) ? "\n Number : " + e.number: "") +
		(isDefined(e.fileName) ? "\n File Name : " + e.fileName: "") +
		(isDefined(e.lineNumber) ? "\n Line Number : " + e.lineNumber: "") +
		(isDefined(e.stack) ? "\n Stack : " + e.stack: "");
	}
	
	
	var setHtmlAttributes = function (htmlTag, attribs) {
		for (var i=0 ; i<attribs.length ; i++) {
			htmlTag.setAttribute(attribs[i][0], attribs[i][1]);	
		}
	}
	
	
	var createCookie = function (name, value, millis, domain) {
		var expires = "";
		if (isDefined(millis)) {
			var date = new Date();
			date.setTime(date.getTime() + millis);
			expires = "; expires=" + date.toGMTString();
		}
		document.cookie = name + "=" + value + expires + (isNotEmptyStr(domain) ? "; domain=" + domain : "") + "";
	}
	
	
	/**
	 * Returns null if no cookie found
	 * @param name
	 * @returns
	 */
	var readCookie = function (name) {
		var nameEQ = name + "=";
		var ca = document.cookie.split(';');
		for (var i=0 ; i<ca.length ; i++) {
			var c = ca[i];
			while (c.charAt(0)==' ') {
				c = c.substring(1, c.length);
			}
			if (c.indexOf(nameEQ) == 0) {
				return c.substring(nameEQ.length, c.length);
			}
		}
		return null;
	}
	
	
	/**
	 * 
	 * @param frameURL can be "about:blank"
	 * @param content
	 * @returns {Boolean}
	 */
	var appendHiddenIFrame = function (frameURL, content) {
		var iframe = document.createElement("IFRAME");
		var attribs = [
		               ["src",frameURL],
		               ["WIDTH","1"],
		               ["HEIGHT","1"],
		               ["MARGINWIDTH","0"],
		               ["MARGINHEIGHT","0"],
		               ["HSPACE","0"],
		               ["VSPACE","0"],
		               ["FRAMEBORDER","0"],
		               ["SCROLLING","no"],
		               ["tabindex","-1"]
		               ];
		setHtmlAttributes(iframe,attribs);
		iframe.style.visibility="hidden";
		document.body.appendChild(iframe); 
		if (isDefined(content)) {
			//Trying to dynamically insert content - may throw security violation exception
			try {
				var iframeDoc = null;
				if (iframe.contentDocument) {
					iframeDoc = iframe.contentDocument;
				}
				else if (iframe.contentWindow) {
					iframeDoc = iframe.contentWindow.document;
				}
				else if (window.frames[iframe.name]) {
					iframeDoc = window.frames[iframe.name].document;
				}
				if (isDefined(iframeDoc)) {
					iframeDoc.open();
					iframeDoc.write(content);
					iframeDoc.close();
					return true;
				}
			}
			catch(e) {
				//on_iiq_error(e);
			}
		}
		return false;
	}
	
	
	var appendImage = function (imageSrc) {
		if (isDefined(imageSrc)) {
			var img = document.createElement("img");
			img.src = imageSrc;
			img.width = "1";
			img.height = "1";
			document.body.appendChild(img);
		}
	}

		
	return {
		
		/* public section */
		
		createIIQPEBElements: function () {
			if (eid == undefined || dpi == undefined) {
				return;	
			}
			
			var block = readCookie("IQBlock");
			if (block != null) {
				return;
			}
			
			var protocol = getProtocol();
			
			var peUrl = protocol + '//api.intentiq.com/profiles_engine/ProfilesEngineServlet';
		        var bidUrl = protocol + '//bid.intentiq.com/profiles_engine/ProfilesEngineServlet';

			var pageUrl = getPageUrl();
			if (pageUrl != "") {
				pageUrl = '&url=' + encodeURIComponent(pageUrl);
			}
		
			var random = '&rnd=' + getRandom(0, 1000000);
		
			var secure = (protocol == "https:") ? 1 : 0;
			
			var iframeUrl = bidUrl + '?at=15&aw=468&ah=60&pagePos=1&vip=true&secure=' + secure + '&eid=' + eid + random + pageUrl;
			
			appendHiddenIFrame(iframeUrl);
		
			var imageSrc = peUrl + '?at=20&mi=10&dpi=' + dpi + '&secure=' + secure + random;
			appendImage(imageSrc);
			
			createCookie("IQBlock", "1", 3600*1000, ".intentiq.com"); //don't come back for 1 hour
		}
	}
})();


IIQ.PEB.createIIQPEBElements();
