// jstools.js !-- coding: utf-8
// CLASSES ----------------------------------------------

function JsRequest() // constructor
{
  // creating xro: (IE does not let extend an XMLHTTP object!)
  if (window.XMLHttpRequest)
    this.xro = new XMLHttpRequest();
  else if (window.ActiveXObject)
    this.xro = new ActiveXObject("Microsoft.XMLHTTP");

  if (!this.xro) return undefined;

  this.url = '';
  this.method = 'GET';
  this.postdata = '';

  this.SyncGet = function(aurl)
  {
    this.url = aurl;
    this.method = 'GET';
    this.xro.open(this.method, aurl, false);
    this.xro.onreadystatechange = function() { }
    this.xro.send(null);

    this.status = this.xro.status;
    this.responseText = this.xro.responseText;
    this.responseXML  = this.xro.responseXML;

    if (this.status == 200)
      return this.xro.responseText;
    else
      return undefined;
  }

  this.SyncPost = function(aurl, adata)
  {
    this.url = aurl;
    this.method = 'POST';
    this.postdata = adata;
    this.xro.open(this.method, aurl, false);
    this.xro.onreadystatechange = function() { }
    this.xro.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
    this.xro.send(this.postdata);

    this.status = this.xro.status;
    this.responseText = this.xro.responseText;
    this.responseXML  = this.xro.responseXML;

    if (this.status == 200)
      return this.xro.responseText;
    else
      return undefined;
  }

  this.PrepareAsyncRequest = function()
  {
    var rqobj = this;  // old windows firefox workaround

    this.xro.onreadystatechange = function()
    {
      if (rqobj.xro.readyState == 4)
      {
        rqobj.status = rqobj.xro.status;

        if (rqobj.status == 200)
        {
          rqobj.responseText = rqobj.xro.responseText;
          rqobj.responseXML  = rqobj.xro.responseXML;
        }
        else
        {
          rqobj.responseText = undefined;
          rqobj.responseXML  = undefined;
        }

        if (typeof(rqobj.OnLoad) == 'function')
        {
          rqobj.OnLoad(rqobj.status == 200);
        }
      }
    }
  }

  this.AsyncGet = function(aurl)
  {
    this.url = aurl;
    this.method = 'GET';
    this.xro.open(this.method, aurl, true);
    this.PrepareAsyncRequest();
    this.xro.send(null);
  }

  this.AsyncPost = function(aurl)
  {
    this.url = aurl;
    this.method = 'POST';
    this.postdata = adata;
    this.xro.open(this.method, aurl, true);
    this.xro.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
    this.PrepareAsyncRequest();
    this.xro.send(this.postdata);
  }

}

function JsQuery(arqpage) // constructor
{
  this.rqpage = arqpage;
  this.rqobj = new JsRequest();

  this.Reset = function()
  {
    this.error = 0;
    this.errormsg = '';
    this.rawdata = [];
    this.data = [];
    this.resultobj = undefined;
  }

  this.Reset();

  this.SyncGet = function(rqp)
  {
    this.Reset();
    var rqs = rqp;
    if (this.rqpage != '') rqs = this.rqpage+'?'+rqs;
    this.rqobj.OnLoad = undefined;
    var s = this.rqobj.SyncGet(rqs);
    return this.ProcessResponse(s);
  }

  this.SyncPost = function(rqp, adata)
  {
    this.Reset();
    var rqs = rqp;
    if (this.rqpage != '') rqs = this.rqpage+'?'+rqs;
    this.rqobj.OnLoad = undefined;
    var s = this.rqobj.SyncPost(rqs, adata);
    return this.ProcessResponse(s);
  }

  this.PrepareAsyncRequest = function()
  {
    var jsqobj = this;

    this.rqobj.OnLoad = function(asuccess)
    {
      var result = jsqobj.ProcessResponse(jsqobj.rqobj.responseText);
      if (typeof(jsqobj.OnLoad) == 'function')
      {
        jsqobj.OnLoad(result);
      }
    }
  }

  this.AsyncGet = function(rqp)
  {
    this.Reset();
    var rqs = rqp;
    if (this.rqpage != '') rqs = this.rqpage+'?'+rqs;
    this.PrepareAsyncRequest();
    this.rqobj.AsyncGet(rqs);
  }

  this.AsyncPost = function(rqp, adata)
  {
    this.Reset();
    var rqs = rqp;
    if (this.rqpage != '') rqs = this.rqpage+'?'+rqs;
    this.PrepareAsyncRequest();
    var s = this.rqobj.AsyncPost(rqs, adata);
  }


  this.ProcessResponse = function(s)
  {
    if (s === undefined)
    {
      // some comm error
      this.error = this.rqobj.status;
      this.errormsg = 'http request errror: <br>'+this.rqobj.responseText;
    }
    else if (s == '')
    {
      this.error = 32;
      this.errormsg = 'JSQuery: Empty result!';
    }
    else
    {
      try
      {
        eval('this.rawdata = '+s+';');
      }
      catch(err)
      {
        this.error = 33;
        this.errormsg = 'JSQuery parsing error: <span style="color: red">'+err.message+'</span><br>'+s;
      }
    }

    if (this.error != 0) return false;

    // check the result
    if ((typeof(this.rawdata) != 'object') || (Object.prototype.toString.apply(this.rawdata) !== '[object Array]'))
    {
      this.error = 34;
      this.errormsg = 'JSQuery: Invalid result! (array expected)<br><br>'+s;
      return false
    }
    else if (this.rawdata.length < 1)
    {
      this.error = 35;
      this.errormsg = 'JSQuery: Invalid result, empty first row<br><br>'+s;
      return false
    }

    this.headobj = this.rawdata.shift(); // remove the first object
    this.error = this.headobj.error;
    this.errormsg = this.headobj.errormsg;
    this.fields = this.headobj.fields;

    this.data = [];
    if (this.rawdata.length > 0)
    {
      for (var i in this.rawdata)
      {
        var d = {};
        for (var fi in this.fields)
        {
          d[this.fields[fi]] = this.rawdata[i][fi];
        }
        this.data[i] = d;
      }
    }

    if (this.error == 0)
      return true;
    else
      return false;
  }
}

function GetPageScrollXOffset()
{
  if ( window.pageXOffset )
    return window.pageXOffset;
  else if ( document.documentElement && document.documentElement.scrollLeft )
    return document.documentElement.scrollLeft;
  else if ( document.body )
    return document.body.scrollLeft;
}

function GetPageVisibleWidth()
{
  if ( window.innerWidth )
    return window.innerWidth;
  else if ( document.documentElement && document.documentElement.clientWidth )
    return document.documentElement.clientWidth;
  else if ( document.body )
    return document.body.clientWidth;
}

function GetPageScrollYOffset()
{
  if ( window.pageYOffset )
    return window.pageYOffset;
  else if ( document.documentElement && document.documentElement.scrollTop )
    return document.documentElement.scrollTop;
  else if ( document.body )
    return document.body.scrollTop;
}

function GetPageVisibleHeight()
{
  if ( window.innerHeight )
    return window.innerHeight;
  else if ( document.documentElement && document.documentElement.clientHeight )
    return document.documentElement.clientHeight;
  else if ( document.body )
    return document.body.clientHeight;
}

var jswindows = {}; // list of JSWindows

// create a jswindowhtml variable in the global scope to overwrite this!
var jswindowhtml_default = '\
<table class="windowbackground" style="width:100%; height:100%;" border="0" cellspacing="0" cellpadding="0">\
  <tr>\
    <td colspan="3" class="windowtitle">\
      <table border="0" cellspacing="0" cellpadding="4" width="100%">\
        <tr>\
          <td onmousedown="dragMouseDown(event)" style="cursor: move;">TITLE</td>\
          <td width="20px"><input type="button" value="&nbsp;X&nbsp;" onclick="jswindows.NAME.OnClose()"></td>\
        </tr>\
      </table>\
    </td>\
  </tr>\
  <tr>\
    <td width="4px" class="windowborder">&nbsp;</td>\
    <td valign="top"><div id="CONTENTID">CONTENT</div></td>\
    <td width="4px" class="windowborder">&nbsp;</td>\
  </tr>\
  <tr height="4px"><td colspan="3" class="windowborder"></td></tr>\
</table>\
';

function JsWindow(aname, atitle, acontent) // constructor
{
  this.name = aname;
  this.title = atitle;
  this.content = acontent;

  this.left = 0;
  this.top  = 0;
  this.width = 500;
  this.height = -1;
  this.windiv = undefined;
  this.contentdiv = undefined;
  this.zindex = 1;
  this.visible = false;

  jswindows[aname] = this;

  this.Show = function()
  {
    if (this.visible) this.Hide();

    this.divtag = document.createElement("div");
    this.divtag.id = this.name;
    this.contentid = this.name+'_CDIV';

    this.divtag.style.position = 'absolute';
    this.divtag.style.left = this.left+'px';
    this.divtag.style.top = this.top+'px';
    this.divtag.style.width = this.width+'px';
    if (this.height > 0)
      this.divtag.style.height = this.height+'px';
    else
      this.divtag.style.height = '';

    this.divtag.style.zIndex = this.zindex;

    var s = jswindowhtml_default;

    if (typeof(jswindowhtml) == 'string')
      s = jswindowhtml;

    s = s.replace(/TITLE/g, this.title);
    s = s.replace(/NAME/g, this.name);
    s = s.replace(/CONTENTID/g, this.contentid);
    s = s.replace(/CONTENT/g, this.content);

    this.divtag.innerHTML = s;
    document.body.appendChild(this.divtag);
    this.visible = true;

    this.contenttag = document.getElementById(this.contentid);
  }

  this.Center = function(aleftobj, atopobj)
  {
    if (typeof(aleftobj) == 'string')
      aleftobj = document.getElementById(aleftobj);

    if (typeof(atopobj) == 'string')
      atopobj = document.getElementById(atopobj);

    var vleft = undefined;
    var vwidth = undefined;
    if ((aleftobj == window) || (aleftobj === undefined))
    {
      vleft = GetPageScrollXOffset();
      vwidth = GetPageVisibleWidth();
    }
    else if (typeof(aleftobj) == 'object')
    {
      var gofs = GetGlobalOffset(aleftobj);
      vleft = gofs.left;
      vwidth = aleftobj.offsetWidth;
    }

    var vtop = undefined;
    var vheight = undefined;
    if ((atopobj == window) || (atopobj === undefined))
    {
      vtop = GetPageScrollYOffset();
      vheight = GetPageVisibleHeight();
    }
    else if (typeof(atopobj) == 'object')
    {
      var gofs = GetGlobalOffset(atopobj);
      vtop = gofs.top;
      vheight = aleftobj.offsetHeight;
    }

    if ((vleft !== undefined) && (vwidth !== undefined))
    {
      var newleft = (vleft + (vwidth - this.divtag.offsetWidth) / 2);
      if (newleft < 0) newleft = 0;
      this.divtag.style.left = newleft + 'px';
    }

    if ((vtop !== undefined) && (vheight !== undefined))
    {
      var newtop = (vtop + (vheight - this.divtag.offsetHeight) / 2);
      if (newtop < 0) newtop = 0;
      this.divtag.style.top =  newtop + 'px';
    }
  }

  this.MoveTo = function(aleft, atop)
  {
    if (aleft !== undefined && aleft != null)
    {
      this.divtag.style.left = aleft + 'px';
    }

    if (atop !== undefined && atop != null)
    {
      this.divtag.style.top = atop + 'px';
    }
  }

  this.ShowAt = function(aleft, atop)
  {
    if (this.visible) this.Hide();
    this.left = aleft;
    this.top  = atop;
    this.Show();
  }

  this.AdjustTop = function()
  {
    var vheight = GetPageVisibleHeight();
    var vtop = GetPageScrollYOffset();

    if (vheight === undefined) vheight = 0;
    if (vtop === undefined) vtop = 0;

    if ( (vheight > 0) && (vtop + vheight < this.divtag.offsetTop + this.divtag.offsetHeight) )
    {
      var newtop = vheight - this.divtag.offsetHeight + vtop;

      if (newtop < 0)
        newtop = vtop;

      this.MoveTo(null, newtop);
    }
  }

  this.Hide = function()
  {
    if (!this.visible) return;

    this.left = this.divtag.offsetLeft;
    this.top = this.divtag.offsetTop;

    document.body.removeChild(this.divtag);
    this.divtag = undefined;
    this.visible = false;
  }

  this.OnClose = function()
  {
    this.Hide();
  }

  this.FormatPost = function() // input fields must be enclosed in <form> tags
  {
    var result = '';
    var forms = this.contentdiv.getElementsByTagName('form');
    for (var fi in forms)
    {
      var f = forms[fi];
      result += FormatPostByForm(f);
    }
    return f;
  }
}


// Functions -----------------------------------------------------

function NumberVal(n, defval)
{
  var n2 = Number(n)
  if (!isNaN(n2)) return n2;
  if (isNaN(defval)) return 0;
  return defval;
}

function StringVal(s, defval)
{
  if ((s !== undefined) && (s != null))
  {
    return String(s);
  }
  if (defval !== undefined) return defval;
  return '';
}

function XmlStr(astr)
{
  var result = new String(astr);

  result = result.replace(/&/g, "&#38;");
  result = result.replace(/</g, "&#60;");
  result = result.replace(/>/g, "&#62;");
  result = result.replace(/'/g, "&#39;");
  result = result.replace(/"/g, "&#34;");

  return result;
}

function XmlStrQ(astr)
{
  return "'" + XmlStr(astr) + "'";
}

function JsStrNq(astr)
{
  var result = new String(astr);

  result = result.replace(/\\/g, "\\x5C");

  result = result.replace(/\x08/g, "\\x08");
  result = result.replace(/\t/g, "\\t");
  result = result.replace(/\n/g, "\\n")
  result = result.replace(/\f/g, "\\f");
  result = result.replace(/\r/g, "\\r");
  result = result.replace(/\v/g, "\\v");

  result = result.replace(/"/g, "\\x22");
  result = result.replace(/&/g, "\\x26");
  result = result.replace(/'/g, "\\x27");
  result = result.replace(/</g, "\\x3C");
  result = result.replace(/>/g, "\\x3E");

  return result;
}

function JsStr(astr)
{
  return "'" + JsStrNq(astr) + "'";
}

function FormatPostByIdList(astr) // astr: 'FIELDID1, FIELDID2, FIELDID3'
{
  var s = String(astr);
  s = s.replace(/\s/g,''); // remove spaces
  var idarr = s.split(',');
  return FormatPostByIdArray(idarr);
}

function FormatPostByIdArray(idarr)
{
  var s = '';
  var idx;
  for (idx in idarr)
  {
    var id = idarr[idx];
    var e = document.getElementById(id);
    if (e)
    {
      var value = FormElementValue(e);
      if (value !== undefined)
      {
  			s += id +'='+ encodeURIComponent(value) +'&';
      }
    }
  }
	return s;
}

function FormatPostByForm(aform)
{
	var s = '';
	for (var i=0; i<aform.elements.length; i++)
	{
		var key = aform.elements[i].name;
		var value = FormElementValue(aform.elements[i]);
		if (key && (value !== undefined))
		{
			s += key +'='+ encodeURIComponent(value) +'&';
		}
	}
	return s;
}

function FormElementValue(formElement)
{
  var type;
	if (formElement.length != null) type = formElement[0].type;
	if ((typeof(type) == 'undefined') || (type == 0)) type = formElement.type;

	switch(type)
	{
		case 'undefined':
    case 'button':
      return undefined;

		case 'radio':
			for (var x=0; x < formElement.length; x++)
				if (formElement[x].checked == true)
			    return formElement[x].value;

		case 'select-multiple':
			var myArray = new Array();
			for (var x=0; x < formElement.length; x++)
				if (formElement[x].selected == true)
					myArray[myArray.length] = formElement[x].value;
			return myArray;

		case 'checkbox':
      return (formElement.checked ? 1 : 0);

		default:
      return formElement.value;
	}
}

function FindParentDiv(obj)
{
  while (obj)
  {
    if (obj.tagName.toUpperCase() == "DIV") return obj;

    if (obj.parentNode)
      obj = obj.parentNode;
    else
      return null;
  }
  return null;
}

function GetGlobalOffset(element)
{
  var gofs = { left:element.offsetLeft, top:element.offsetTop };

  var pElement = element.offsetParent;

  while (pElement != document.body)
  {
    gofs.left += pElement.offsetLeft
    gofs.top  += pElement.offsetTop
    pElement = pElement.offsetParent
  }
  return gofs;
}


function iif(acond, avalt, avalf)
{
  if (acond)
    return avalt;
  else
    return avalf;
}

function SetTableRowVisible(arowobj, avisible)
{
  if (!arowobj) return;

  if (document.all) // IE?
  {
    arowobj.style.display = ( avisible ? 'block' : 'none' );
  }
  else
  {
    arowobj.style.display = ( avisible ? 'table-row' : 'none' );
  }
}

function FormatLink(atarget, atext, aextra)
{
  return '<a href="'+XmlStr(atarget)+'" '+aextra+'>'+atext+'</a>';
}

function FormatButton(aname, avalue, aparams)
{
  return '<input type="button" id="'+aname+'" name="'+aname+'" '+aparams+' value="'+avalue+'">';
}

function FormatInputText(aname, avalue, aparams)
{
  return '<input type="text" id="'+aname+'" name="'+aname+'" '+aparams+' value="'+avalue+'">';
}

function FormatTextArea(aname, avalue, aparams)
{
  return '<textarea id="'+aname+'" name="'+aname+'" '+aparams+' >'+avalue+'</textarea>';
}

function FormatCheckBox(aname, avalue, aparams)
{
  var s = '<input type="checkbox" id="'+aname+'" name="'+aname+'" value="1" '+aparams;
  if (avalue) s += ' checked';
  s += ' />';
  return s;
}

function FormatSelect(aname, defvalue, objarr, idfield, namefield, aparams)
{
  var s = '<select id="'+aname+'" name="'+aname+'" '+aparams+'>';
  s += FormatSelectOptions(objarr, idfield, namefield, defvalue);
  s += '</select>';
  return s;
}

function FormatSelectAssoc(aname, defvalue, objassoc, aparams)
{
  var s = '<select id="'+aname+'" name="'+aname+'" '+aparams+'>';
  for (var pn in objassoc)
  {
    s += '<option value="'+pn+'"';
    if (pn == defvalue) s += ' selected';
    s += '>'+objassoc[pn]+'</option>';
  }
  s += '</select>';
  return s;
}

function FormatSelectOptions(objarr, idfield, namefield, defvalue) // ([{id:1,name:'first'},{id:2,name:'second'}],'id','name',2)
{
  var s = '';
  var pn;
  for (pn in objarr)
  {
    var o = objarr[pn];
    s += '<option value="'+o[idfield]+'"';
    if (o[idfield] == defvalue) s += ' selected';
    s += '>'+o[namefield]+'</option>';
  }
  return s;
}

function HSpace(awidth)
{
  return '<span style="margin-left:'+awidth+'px"></span>';
}

function VSpace(aheight)
{
  return '<div style="height:'+aheight+'px"></div>';
}

function DateFromSql(astr)
{
  if (!astr) return null;

  var s = StringVal(astr);
  var y = s.substr(0,4);
  var m = s.substr(5,2);
  var d = s.substr(8,2);
  var t = s.substr(11,8);
  var ds = m+'/'+d+'/'+y+' '+t;
  //console.log(ds);
  var d = new Date();
  d.setTime(Date.parse(ds));
  return d;
}

function IsIE()
{
  return (document.all ? true : false);
}

// date.format.js

/*
 * Date Format 1.2.3
 * (c) 2007-2009 Steven Levithan <stevenlevithan.com>
 * MIT license
 *
 * Includes enhancements by Scott Trenda <scott.trenda.net>
 * and Kris Kowal <cixar.com/~kris.kowal/>
 *
 * Accepts a date, a mask, or a date and a mask.
 * Returns a formatted version of the given date.
 * The date defaults to the current date/time.
 * The mask defaults to dateFormat.masks.default.
 */

var DateFormat = function ()
{
	var	token = /d{1,4}|m{1,4}|yy(?:yy)?|([HhMsTt])\1?|[LloSZ]|"[^"]*"|'[^']*'/g,
		timezone = /\b(?:[PMCEA][SDP]T|(?:Pacific|Mountain|Central|Eastern|Atlantic) (?:Standard|Daylight|Prevailing) Time|(?:GMT|UTC)(?:[-+]\d{4})?)\b/g,
		timezoneClip = /[^-+\dA-Z]/g,
		pad = function (val, len) {
			val = String(val);
			len = len || 2;
			while (val.length < len) val = "0" + val;
			return val;
		};

	// Regexes and supporting functions are cached through closure
	return function (date, mask, utc) {
		var dF = DateFormat;

		// You can't provide utc if you skip other args (use the "UTC:" mask prefix)
		if (arguments.length == 1 && Object.prototype.toString.call(date) == "[object String]" && !/\d/.test(date)) {
			mask = date;
			date = undefined;
		}

		// Passing date through Date applies Date.parse, if necessary
		date = date ? new Date(date) : new Date;
		if (isNaN(date)) throw SyntaxError("invalid date");

		mask = String(dF.masks[mask] || mask || dF.masks["default"]);

		// Allow setting the utc argument via the mask
		if (mask.slice(0, 4) == "UTC:") {
			mask = mask.slice(4);
			utc = true;
		}

		var	_ = utc ? "getUTC" : "get",
			d = date[_ + "Date"](),
			D = date[_ + "Day"](),
			m = date[_ + "Month"](),
			y = date[_ + "FullYear"](),
			H = date[_ + "Hours"](),
			M = date[_ + "Minutes"](),
			s = date[_ + "Seconds"](),
			L = date[_ + "Milliseconds"](),
			o = utc ? 0 : date.getTimezoneOffset(),
			flags = {
				d:    d,
				dd:   pad(d),
				ddd:  dF.i18n.dayNames[D],
				dddd: dF.i18n.dayNames[D + 7],
				m:    m + 1,
				mm:   pad(m + 1),
				mmm:  dF.i18n.monthNames[m],
				mmmm: dF.i18n.monthNames[m + 12],
				yy:   String(y).slice(2),
				yyyy: y,
				h:    H % 12 || 12,
				hh:   pad(H % 12 || 12),
				H:    H,
				HH:   pad(H),
				M:    M,
				MM:   pad(M),
				s:    s,
				ss:   pad(s),
				l:    pad(L, 3),
				L:    pad(L > 99 ? Math.round(L / 10) : L),
				t:    H < 12 ? "a"  : "p",
				tt:   H < 12 ? "am" : "pm",
				T:    H < 12 ? "A"  : "P",
				TT:   H < 12 ? "AM" : "PM",
				Z:    utc ? "UTC" : (String(date).match(timezone) || [""]).pop().replace(timezoneClip, ""),
				o:    (o > 0 ? "-" : "+") + pad(Math.floor(Math.abs(o) / 60) * 100 + Math.abs(o) % 60, 4),
				S:    ["th", "st", "nd", "rd"][d % 10 > 3 ? 0 : (d % 100 - d % 10 != 10) * d % 10]
			};

		return mask.replace(token, function ($0) {
			return $0 in flags ? flags[$0] : $0.slice(1, $0.length - 1);
		});
	};
}();

// Some common format strings
DateFormat.masks = {
	"default":      "ddd mmm dd yyyy HH:MM:ss",
	shortDate:      "m/d/yy",
	mediumDate:     "mmm d, yyyy",
	longDate:       "mmmm d, yyyy",
	fullDate:       "dddd, mmmm d, yyyy",
	shortTime:      "h:MM TT",
	mediumTime:     "h:MM:ss TT",
	longTime:       "h:MM:ss TT Z",
	isoDate:        "yyyy-mm-dd",
	isoTime:        "HH:MM:ss",
	isoDateTime:    "yyyy-mm-dd'T'HH:MM:ss",
	isoUtcDateTime: "UTC:yyyy-mm-dd'T'HH:MM:ss'Z'"
};

// Internationalization strings
DateFormat.i18n = {
	dayNames: [
		"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat",
		"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"
	],
	monthNames: [
		"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec",
		"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"
	]
};

// For convenience...
Date.prototype.format = function (mask, utc) {
	return DateFormat(this, mask, utc);
};

// end date.format.js

// Helper functions ----------------------------------------------

var dragObjTitle = null;
var dragOffsetX = 0;
var dragOffsetY = 0;

function preventEventDefault(e)
{
  if (e.preventDefault)
  {
    e.preventDefault();
  }
  else
  {
    //document.onselectstart = function () { return false; };
    e.cancelBubble = true;
    return false;
  }
}

function dragMouseDown(e)
{
  var curElem = e.srcElement || e.target;
  var dragTitle = FindParentDiv(curElem);

  if (!curElem || !dragTitle) return;

  dragObjTitle = dragTitle;

  dragOffsetX = e.clientX - dragTitle.offsetLeft;
  dragOffsetY = e.clientY - dragTitle.offsetTop;

  if (document.body.addEventListener)
  {
    document.onmousemove = dragMouseMove;
    document.onmouseup = dragMouseUp;
  }
  else // IE mode
  {
    document.onmousemove = function() { dragMouseMove(window.event) } ;
    document.onmouseup = function() { dragMouseUp(window.event) } ;
  }

  return preventEventDefault(e);
}

function dragMouseMove(e)
{
  if (!dragObjTitle) return;

  dragObjTitle.style.left = (e.clientX - dragOffsetX)+'px';
  dragObjTitle.style.top = (e.clientY - dragOffsetY)+'px';

  return preventEventDefault(e);
}

function dragMouseUp(e)
{
  if (!dragObjTitle) return;

  var finalX = e.clientX - dragOffsetX;
  var finalY = e.clientY - dragOffsetY;
  if (finalX < 0) { finalX = 0 };
  if (finalY < 0) { finalY = 0 };

  dragObjTitle.style.left = (finalX)+'px';
  dragObjTitle.style.top = (finalY)+'px';

  dragObjTitle = null;

  document.onmousemove = null;
  document.onmouseup = null;

  return preventEventDefault(e);
}

var lightbox_hidden_elements = [];

function LightBoxOpen(content)
{
  var fullheight = document.body.scrollHeight;

  var vheight = GetPageVisibleHeight();
  var vtop = GetPageScrollYOffset();

  var divtag = document.createElement('div');
  divtag.id = 'LIGHT_BOX_DIV';
  divtag.style.position = 'absolute';
  divtag.style.width = '100%';
  divtag.style.height = fullheight+'px';
  divtag.style.top = '0px';
  divtag.style.left = '0px';
  divtag.style.background = 'black';
  divtag.style['opacity'] = 0.5;                 // every normal browser
  divtag.style['filter'] = 'alpha(opacity=50)';  // MSIE
  divtag.style.zIndex = 99998;
  divtag.innerHTML = '';

  document.body.appendChild(divtag);

  var divtag = document.createElement('div');
  divtag.id = 'LIGHT_BOX_TAB';
  divtag.style.position = 'absolute';
  divtag.style.width = '100%';
  divtag.style.height = vheight+'px';
  divtag.style.top = vtop+'px';
  divtag.style.left = '0px';
  divtag.style.zIndex = 99999;
  document.body.appendChild(divtag);

  divtag.innerHTML =
    '<table style="height:100%; width:100%;" border="0" cellpadding="0" cellspacing="0">'+
      '<tr>'+
        '<td width="100%" height="100%" align="center" valign="middle">'+
          '<div id="LIGHT_BOX_CONTENT">'+content+'</div>'+
        '</td>'+
      '</tr>'+
    '</table>'
  ;

  lightbox_hidden_elements = [];

  var embeds = document.getElementsByTagName('EMBED');
  for (var i = 0; i < embeds.length; i++)
  {
    if ((embeds[i].type == 'application/x-shockwave-flash') && (embeds[i].getAttribute('wmode') != 'transparent'))
    {
      lightbox_hidden_elements.push( {element:embeds[i], display:embeds[i].style.display} );
      embeds[i].style.display = 'none';
    }
  }

  var objects = document.getElementsByTagName('OBJECT');
  for (var i = 0; i < objects.length; i++)
  {
    if (objects[i].WMode != 'Transparent')
    {
      lightbox_hidden_elements.push( {element:objects[i], display:objects[i].style.display} );
      objects[i].style.display = 'none';
    }
  }
}

function LightBoxClose()
{
  var divtag = document.getElementById('LIGHT_BOX_DIV');
  document.body.removeChild(divtag);
  var divtag = document.getElementById('LIGHT_BOX_TAB');
  document.body.removeChild(divtag);

  for (var i = 0; i < lightbox_hidden_elements.length; i++)
  {
    lightbox_hidden_elements[i].element.style.display = lightbox_hidden_elements[i].display;
  }

  lightbox_hidden_elements = [];
}


function AddLoadEvent() // usage: AddLoadEvent(funcToCall, [param1..paramN]);
{
  var oldonload = window.onload;
  var func = arguments[0];
  var args = new Array();

  for (var i = 1; i < arguments.length; i++)
    args.push(arguments[i]);

  window.onload = function()
  {
    if (typeof(oldonload) == 'function')
      oldonload();

    func.apply(this, args);
  }
}

