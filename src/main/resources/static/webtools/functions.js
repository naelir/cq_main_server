
var same = Math.floor(Math.random() * 1000000);

function trim(str)
{
  var tmp = new String(str);
  tmp = tmp.replace(/^\s+/, '');
  tmp = tmp.replace(/\s+$/, '');
  return tmp;
}

function FormatSWFLogo(width, height, elementid)
{
  if (typeof(elementid) == 'undefined')
    elementid = 'LOGOSWF_' + Math.floor(Math.random() * 1000000);

  var so = new SWFObject('swf/swflogo.swf', elementid, width, height, '9');
  so.addParam('allowscriptaccess', 'sameDomain');
  so.addParam('allowfullscreen', 'false');
  so.addParam('quality', 'high');
  so.addParam('menu', 'false');
  so.addParam('wmode', 'transparent');
  so.addVariable('langid', langid);

  return so.getSWFHTML();
}

function FormatSWFWaitAnim(size)
{
  var so = new SWFObject('swf/preloader.swf', 'WAITANIM', size, size, '9');
  so.addParam('allowscriptaccess', 'sameDomain');
  so.addParam('allowfullscreen', 'false');
  so.addParam('wmode', 'transparent');
  so.addParam('quality', 'high');
  so.addParam('menu', 'false');
  return so.getSWFHTML();
}

function FormatCQButton(name, value, id, type, onclick, extra)
{
  if (id === undefined) id = 'ok';
  if (type === undefined) type = 'submit';
  if (onclick === undefined) onclick = '';

  var res = '';
  res += ' <input';
  res += ' class="button_'+id+'"';
  res += ' name="'+name+'"';
  res += ' id="'+name+'"';
  res += ' type="'+type+'"';
  res += ' value="'+XmlStr(value)+'"';
  res += ' onclick="'+onclick+'"';
  res += ' '+StringVal(extra);
  res += ' >';

  return res;
}

var jswindowhtml = '\
<table class="windowbackground" style="width:100%; height:100%;" border="0" cellspacing="0" cellpadding="0">\
  <tr>\
    <td colspan="3" class="windowtitle">\
      <table border="0" cellspacing="0" cellpadding="4" width="100%">\
        <tr>\
          <td width="180">'+FormatSWFLogo(135, 27)+'</td>\
          <td onmousedown="dragMouseDown(event)" style="cursor: move;">TITLE</td>\
          <td width="20px" valign="middle">\
            <input\
              onMouseOut="javascript:className=\'button_ok_noover\';"\
              onMouseOver="javascript:className=\'button_ok_over\';"\
              class="button_ok_noover"\
              type="button"\
              value="X"\
              onclick="jswindows.NAME.OnClose();"\
              style="height:16px; width:16px;"\
            >\
          </td>\
        </tr>\
      </table>\
    </td>\
  </tr>\
  <tr>\
    <td width="4px" class="windowborder">&nbsp;</td>\
    <td valign="top"><div id="CONTENTID" class="windowcontent">CONTENT</div></td>\
    <td width="4px" class="windowborder">&nbsp;</td>\
  </tr>\
  <tr height="4px"><td colspan="3" class="windowborder"></td></tr>\
</table>\
';

//----------------------------------------

var waiting_anim_div = null;

function ShowWaitAnim(size)
{
  if (waiting_anim_div != null) return; // already shown

  if (size === undefined) size = 50;

  var so = new SWFObject('swf/preloader.swf', 'WAITANIM', size, size, '9');
  so.addParam('allowscriptaccess', 'sameDomain');
  so.addParam('allowfullscreen', 'false');
  so.addParam('wmode', 'transparent');
  so.addParam('quality', 'high');
  so.addParam('menu', 'false');

  var divtag = document.createElement('div');
  divtag.id = 'WAIT_ANIM_DIV';
  divtag.style.position = 'absolute';
  divtag.style.width = '100px';
  divtag.style.height = '100px';
  divtag.style.top = (GetPageScrollYOffset() + (Math.round(GetPageVisibleHeight() / 2) - Math.round(size / 2))) + 'px';
  divtag.style.left = (GetPageScrollXOffset() + ((Math.round(GetPageVisibleWidth() - 1000) / 2) + 300 - Math.round(size / 2))) + 'px';
  divtag.style.zIndex = 99997;
  divtag.innerHTML = so.getSWFHTML();

  document.body.appendChild(divtag);
  waiting_anim_div = divtag;
}

function HideWaitAnim()
{
  if (waiting_anim_div == null) return; // nothing to hide

  var divtag = document.getElementById('WAIT_ANIM_DIV');
  document.body.removeChild(divtag);
  waiting_anim_div = null;
}

//----------------------------------------

function FormatToolTip(text, bubbletext, with_style, width, align)
{
  if (with_style === undefined) with_style = true;
  if (width === undefined) width = 140;
  if (align === undefined) align = 'LEFT';

  if (with_style)
    var style = 'class="tooltip"';
  else
    var style = '';

  return '<span '+style+' onmouseout="return nd();" onmouseover="return overlib('+JsStr(bubbletext)+', WIDTH, '+width+', '+align+');">'+text+'</span>';
}

function IsImageOk(img)
{
  // During the onload event, IE correctly identifies any images that
  // weren’t downloaded as not complete. Others should too. Gecko-based
  // browsers act like NS4 in that they report this incorrectly.
  if (!img.complete)
  {
    return false;
  }

  // However, they do have two very useful properties: naturalWidth and
  // naturalHeight. These give the true size of the image. If it failed
  // to load, either of these should be zero.
  if (typeof img.naturalWidth != 'undefined' && img.naturalWidth == 0)
  {
    return false;
  }

  // No other way of checking: assume it’s ok.
  return true;
}

//Call this function onLoad of body tag
function CheckMeasure()
{
  for (var i = 0; i < document.images.length; i++)
  {
    if (document.images[i].src.indexOf('audit.median.hu') > -1)
    {
      if (!IsImageOk(document.images[i]))
      {
        var image = new Image(1, 1);
        image.name = 'checkMeasure';
        image.style.position = 'absolute';
        image.style.top = -20 + 'px';
        image.style.left = -20 + 'px';
        image.src = 'pic/shim.gif?nomeasure=yes&escapecache=' + same;
      }
    }
  }
}

//------------------------------------

function __ShowUserDataPage(userid, title)
{
  var r = new JsRequest();
  var d = r.SyncGet('userdata.php?uid=' + userid);
  var w = new JsWindow('USERDATAWIN', title, d);
  w.Show();
  //~ w.Center('USERNAME_'+userid, 'USERNAME_'+userid);
}

function ShowUserDataPage(userid)
{
  var addrwin = window.open(
    "userdata.php?uid="+userid,
    "userdata",
    "width=360,height=650,scrollbars=yes,resizable=yes,toolbar=no,status=no,menubar=no,location=no,directories=no,copyhistory=no"
  );

  addrwin.opener = self;
  addrwin.focus();
}

function ShowUserBadgePage(userid)
{
  var addrwin = window.open(
    "badges.php?userid="+userid,
    "badges",
    "width=800,height=650,left=361,scrollbars=yes,resizable=yes,toolbar=no,status=no,menubar=no,location=no,directories=no,copyhistory=no"
  );

  addrwin.opener = self;
  addrwin.focus();
}

//------------------------------------

function SetElementStyleClass(elementid, classname)
{
  var o = document.getElementById(elementid);
  o.className = classname;

  if (typeof(supersleight) != 'undefined')
  {
    supersleight.limitTo(elementid);
    supersleight.run();
  }
}

function SetElementVisibility(elementid, visible)
{
  var o = document.getElementById(elementid);

  if (visible)
  {
    o.style.display = 'block';

    if (typeof(supersleight) != 'undefined')
    {
      supersleight.limitTo(elementid);
      supersleight.run();
    }
  }
  else
  {
    o.style.display = 'none';
  }
}

//------------------------------------

function SetForumPostActiveOn(postid, classname)
{
  SetElementStyleClass('POSTHEADER_' + postid, 'forum_post_title' + classname + '_over');
  SetElementVisibility('RESPONSELINK_' + postid, true);
  SetElementVisibility('ADMINHTML_' + postid, true);
}

function SetForumPostActiveOff(postid, classname)
{
  SetElementStyleClass('POSTHEADER_' + postid, 'forum_post_title' + classname);
  SetElementVisibility('RESPONSELINK_' + postid, false);
  SetElementVisibility('ADMINHTML_' + postid, false);
}

//------------------------------------

function HideInfoText()
{
  var rq = new JsRequest();
  var res = rq.SyncGet('js_gateway.php?op=hideinfotext');

  var o = document.getElementById('INFOTEXT');
  o.style.display = 'none';
}

//------------------------------------

function SetupFormInputBoxes(formname, focusname)
{
  var elems = document.forms[formname].elements;

  for (var i = 0; i < elems.length; i++)
  {
    var elem = elems[i];

    if (elem.type == 'text' || elem.type == 'password' || elem.type == 'textarea')
    {
      elem.onblur = new Function('document.forms.'+formname+'.'+elem.name+'.className = "inputbox"');
      elem.onfocus = new Function('document.forms.'+formname+'.'+elem.name+'.className = "inputbox_active"');
      if (elem.name == focusname)
        elem.focus();
      else
        elem.className = 'inputbox';
    }
  }
}

//------------------------------------

function ChangeFileName(newfile)
{
  var url = window.location.href;
  lasti = url.lastIndexOf('/');
  url = url.substring(0, lasti + 1) + newfile;
  return url;
}

//------------------------------------

var client_window = null;

function StartGame()
{
  top.location.href = ChangeFileName('client_afterstart.php');

  if (client_window == null || client_window.closed)
  {
    client_window = window.open(
      ChangeFileName('client_pre_frame.php'),
      'cqclient',
      'width='+(screen.availWidth - 10) + ', height='+(screen.availHeight - 30) + ', resizable=yes, top=0, left=0'
    );
  }

  client_window.focus();

  //~ return true;
}

//------------------------------------

function RefreshContent(url)
{
  if (url.indexOf('noframe') < 0)
  {
    if (url.indexOf('?') < 0)
    {
      url += '?noframe=1';
    }
    else
    {
      url += '&noframe=1';
    }
  }

  var r = new JsRequest();
  var d = r.SyncGet(url);
  var o = document.getElementById('LEFTSIDE');
  //console.log(d);
  o.innerHTML = d;
}

//------------------------------------

var userdataeditwin = null;

function OpenUserDataEdit()
{
  var r = new JsRequest();
  var d = r.SyncGet('account.php?noframe=1');

  userdataeditwin = new JsWindow('USERDATAEDITWIN', '','');
  userdataeditwin.width = 600;
  userdataeditwin.content = d;
  userdataeditwin.Show();
  userdataeditwin.Center(window, window);
}

function CloseUserDataEdit()
{
  userdataeditwin.Hide();
  RefreshContent(document.location.href);
}

//------------------------------------

var help_window = null;

function OpenHelp(id)
{
  if (help_window == null || help_window.closed)
  {
    help_window = window.open(
      ChangeFileName('help.php?tl=' + id),
      'help',
      'width=600,height=' + (screen.availHeight * 0.5) + ',resizable=yes,top=4,left=' + (screen.availWidth - 608) + ',toolbar=yes,scrollbars=yes'
    );
  }
  else
  {
    help_window.location.href = ChangeFileName('help.php?tl=' + id);
    help_window.focus();
  }

  return true;
}

//------------------------------------

var menu_others_div = null;

function ShowOthersMenu()
{
  var omp = GetGlobalOffset(document.getElementById('OTHERS_MENU_LINK'));

  var d = document.createElement("div");
  d.id = 'MENU_OTHERS';

  var leftcorr = 0;
  if (IsIE()) leftcorr = 16;

  d.style.position = 'absolute';
  d.style.left = (omp.left-2+leftcorr)+'px';
  d.style.top = (omp.top-2)+'px';
  d.style.width = '300px';
  d.style.zIndex = 99;

  d.className = 'othersmenu';

  //d.style.backgroundColor = '#391200';
  //d.style.borderWidth = '1px';

  d.onmouseout = function(e)
  {
    if (IsIE())
    {
      var event = window.event;
      var iecorr = 2;
    }
    else
    {
      var event = e;
      var iecorr = 0;
    }

    if (event.clientX-iecorr < this.offsetLeft || event.clientX > this.offsetLeft + this.offsetWidth - 1
        || event.clientY-iecorr < this.offsetTop || event.clientY > this.offsetTop + this.offsetHeight - 1)
    {
      HideOthersMenu();
    }
  }

  var s = '';
  s += '<table border="0" cellpadding="2" cellspacing="0" width="100%" >'; //style="background-color: #391200;">';

  var cnt = 0;
  var addsep = false;
  for (var name in others_menu)
  {
    var value = others_menu[name];

    var st = '';

    cnt ++;
    s += '<tr>';
    if (cnt == 1)
    {
      s += '<td style="cursor: default;border-bottom: 1px solid #885b05;">'+name+'</td>';
    }
    else
    {
      if (value == '-')
      {
        addsep = true;
      }
      else
      {
        if (addsep) st += 'border-top: 1px solid #885b05;';

        if (value.indexOf('http://') == 0)
          target = '_blank';
        else
          target = '_self';

        s += '<td style="'+st+'"><a href="'+value+'" target="'+target+'">'+name+'</a></td>';

        addsep = false;
      }
    }
    s += '</tr>';
  }

  s += '</table>';

  d.innerHTML = s;
  document.body.appendChild(d);

  menu_others_div = d;
}

function HideOthersMenu()
{
  if (menu_others_div)
  {
    document.body.removeChild(menu_others_div);
    menu_others_div = null;
  }
}

//------------------------------------

function PaddingLeft(str, pad, len)
{
  var tmp = new String(str);

  while (tmp.length < len)
    tmp = pad + tmp;

  return tmp;
}

function FormatDate(format, date)
{
  if (format === undefined)
    format = '%Y-%m-%d %H:%M:%S';

  if (date === undefined)
    var now = new Date();
  else
    var now = date;

  var tmp = new String(format);

  tmp = tmp.replace(/%Y/g, PaddingLeft(now.getFullYear(), '0', 4));
  tmp = tmp.replace(/%m/g, PaddingLeft(now.getMonth() + 1, '0', 2));
  tmp = tmp.replace(/%d/g, PaddingLeft(now.getDate(), '0', 2));

  tmp = tmp.replace(/%H/g, PaddingLeft(now.getHours(), '0', 2));
  tmp = tmp.replace(/%M/g, PaddingLeft(now.getMinutes(), '0', 2));
  tmp = tmp.replace(/%S/g, PaddingLeft(now.getSeconds(), '0', 2));

  tmp = tmp.replace(/%[bB]/g, getlang('month_'+(now.getMonth() + 1)));

  tmp = tmp.replace(/\[/g, '');
  tmp = tmp.replace(/\]/g, '');

  return tmp;
}

function FormatNumber(anumber, adecimals)
{
  var ts = '&nbsp;'; // thousand separator
  var decs = 0;
  if (adecimals !== undefined)  decs = Number(adecimals);

  var numstr = String( Math.round(anumber * Math.pow(10,decs)) / Math.pow(10,decs) );

  var parts = numstr.split('.');
  if (parts.length < 2)  parts[1] = '';

  parts[0] = parts[0].replace(/(\d)(?=(\d{3})+$)/g,"$1"+ts);

  while (parts[1].length < decs) parts[1] += '0';

  if (decs > 0)  parts[0] += '.' + parts[1];

  return parts[0];
}

function FormatMessageBox(message, color)
{
  if (typeof(color) == 'undefined') color = 'red';

  if (color != 'blue' && color != 'green' && color != 'red' && color != 'yellow') color = 'red'; // default

  var res = '';

  res += '<table border="0" width="576" cellspacing="0" cellpadding="0">';
  res += '<tr>';
  res += '<td class="box_'+color+'_header"></td>';
  res += '</tr>';
  res += '<tr>';
  res += '<td align="center" valign="middle" class="box_'+color+'_body"><div style="padding-left:10px;padding-right:10px;padding-bottom:10px;">'+message+'</div></td>';
  res += '</tr>';
  res += '<tr>';
  res += '<td class="box_'+color+'_footer"></td>';
  res += '</tr>';
  res += '</table>';

  return res;
}

//--------------------------------------------------------------------------------------

function FixPng(idname)
{
  if (document.all && (parseInt(navigator.appVersion) < 7) && (typeof(supersleight) != 'undefined')) // IE
  {
    supersleight.limitTo(idname);
    supersleight.run();
  }
}

function CLPBox(infoText, urlCancel)
{
  var text = infoText;

  text += '<br><br>'+FormatCQButton('btnOK', getlang('ok'), 'ok', 'button', 'javascript:document.location.href='+JsStr(urlCancel));

  var content = '';

  content += '<table border="0" cellspacing="0" cellpadding="0">';
  content += '<tr>';

  content += '<td>';
  content += '<div style="padding: 8px; width:500px; border: 3px solid #202020; background-color: #080808; font-size: 120%; text-align: center;">';
  content += text;
  content += '</div>';
  content += '</td>';

  content += '<td width="340">';
  content += '&nbsp;';
  content += '</td>';

  content += '</tr></table>';

  LightBoxOpen(content);
  //FixPng('LIGHT_BOX_CONTENT');
}

function InfoBox(acontent, aonclose)
{
  var onclose = StringVal(aonclose);

  var content = '';
  content += '<table border="0" cellspacing="0" cellpadding="0">';
  content += '<tr>';

  content += '<td>';
  content += '<div style="padding: 8px; width:580px; border: 3px solid #202020; background-color: #080808; font-size: 100%; text-align: center;">';

  content += acontent;
  content += '<br><br>'+FormatCQButton('btnOK', getlang('ok'), 'ok', 'button', 'javascript:LightBoxClose();'+onclose);

  content += '</div>';
  content += '</td>';

  content += '<td width="360">';
  content += '&nbsp;';
  content += '</td>';

  content += '</tr></table>';

  LightBoxOpen(content);
  //FixPng('LIGHT_BOX_CONTENT');
}

function CheckCLPLimit(viplimit,oncontinue)
{
  if ((gmydata.id > 0) && ((gmydata.rights & 0x86) || NumberVal(gmydata.vip) > 0))
  {
    return true;
  }

  if (viplimit <= 1 || viplimit <= gmydata.clp)
  {
    return true;
  }

  var onclose = 'javascript:LightBoxClose();';

  viptext = getlang('clp_required', viplimit);
  viptext = '<p style="font-size: 150%; font-weight: bold; color: #FFFFFF;">'+viptext+'</p>'
    + '<p><b>'+getlang('your_current_clp', gmydata.clp)+'</b></p>'
    + getlang('clp_explain_short');

  CLPBox(viptext, onclose);

  return false;
}

function openAuctHistory(itemid)
{
  var histwin = window.open("/htmlcache/aucthist/item"+itemid+".html", "aucthist", "width=380,height=500,scrollbars=yes,resizable=yes,toolbar=no,status=no,menubar=no");
  histwin.opener = self;
  histwin.focus();
}

function FaceBookConnect(el)
{
  fb_connect = window.open('fb_connect.php?ret='+window.location.hostname, 'fb_connect', 'height=500,width=600,left=20,top=20,menubar=no,toolbar=no,location=no,status=no,resizable=no');

  if (window.focus) { fb_connect.focus() }
}
