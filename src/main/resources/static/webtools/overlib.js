// onMouseOver="showToolTip(event, 'texttodisplay');" onMouseOut="hideToolTip(event);" onMouseMove="moveToolTip(event);"

var toolTipWidth = 140;
var toolTipTextColor = '#6E0D0D';
var toolTipTextBackground = '#FFFFCC';
var toolTipOffsetX = 10; // to the mice ptr
var toolTipOffsetY = 20; // to the mice ptr
var toolTipShadowOpacity = 50;
var toolTipShadowOffsetX = 5;
var toolTipShadowOffsetY = 5;

//-------------------------------------------------------------------

document.write(
  '<div id="toolTipDiv" style="position:absolute; left:0; top:0; z-index:9999; visibility:hidden;">'+
    '<table bgcolor="#000000" cellpadding="0" cellspacing="1" border="0">'+
      '<tr>'+
        '<td>'+
          '<table style="background-color:'+toolTipTextBackground+'; color: '+toolTipTextColor+';" border="0" cellpadding="2">'+
            '<tr>'+
              '<td id="toolTipWidth"><span id="toolTipText">&nbps;</span></td>'+
            '</tr>'+
          '</table>'+
        '</td>'+
      '</tr>'+
    '</table>'+
  '</div>'+
  '<div id="toolTipShadowDiv" style="position:absolute; left:0; top:0; z-index:9998; visibility:hidden;">'+
    '<table bgcolor="#000000" cellpadding="0" cellspacing="1" border="0">'+
      '<tr>'+
        '<td>'+
          '<table bgcolor="#000000" border="0" cellpadding="2">'+
            '<tr>'+
              '<td id="toolTipShadowWidth"><span id="toolTipShadowText">&nbsp;</span></td>'+
            '</tr>'+
          '</table>'+
        '</td>'+
      '</tr>'+
    '</table>'+
  '</div>'
);

//-------------------------------------------------------------------

document.getElementById('toolTipShadowDiv').style['opacity'] = toolTipShadowOpacity / 100;
document.getElementById('toolTipShadowDiv').style['filter'] = 'alpha(opacity='+toolTipShadowOpacity+');';

var toolTipNS4 = (navigator.appName == 'Netscape' && parseInt(navigator.appVersion) == 4) ? 1 : 0;
var toolTipIE = document.all ? 1 : 0;

var INARRAY=1, WIDTH=2, ABOVE=3, LEFT=4, MIDDLE=5, CENTER=5, RIGHT=6;

var toolTipCurrentText = '';
var toolTipCurrentWidth = toolTipWidth;
var toolTipCurrentAbove = false;
var toolTipCurrentLeft = false;
var toolTipCurrentMiddle = false;

//-------------------------------------------------------------------

function nd(e)
{
  if (toolTipIE)
    document.onmousemove = null;
  else
    window.onmousemove = null;

  document.getElementById('toolTipDiv').style.visibility = 'hidden';
  document.getElementById('toolTipShadowDiv').style.visibility = 'hidden';

  return true;
}

function overlibMouseMove(e)
{
  var posx = 0;
  var posy = 0;

  if (toolTipIE)
  {
    posx = event.clientX + (document.documentElement.scrollLeft ? document.documentElement.scrollLeft : document.body.scrollLeft);
    posy = event.clientY + (document.documentElement.scrollTop ? document.documentElement.scrollTop : document.body.scrollTop);
  }
  else
  {
    posx = e.pageX;
    posy = e.pageY;
  }

  var obj = document.getElementById('toolTipDiv');

  var diffx = toolTipOffsetX;
  var diffy = toolTipOffsetY;

  if (toolTipCurrentLeft)
    diffx = (toolTipOffsetX + obj.clientWidth) * -1;

  if (toolTipCurrentMiddle)
    diffx = (obj.clientWidth / 2) * -1;

  if (toolTipCurrentAbove)
    diffy = (toolTipOffsetY + obj.clientHeight) * -1;

  obj.style.left = (posx + diffx) + 'px';
  obj.style.top = (posy + diffy) + 'px';
  obj.style.visibility = 'visible';

  var obj = document.getElementById('toolTipShadowDiv');
  obj.style.left = (posx + diffx) + toolTipShadowOffsetX + 'px';
  obj.style.top = (posy + diffy) + toolTipShadowOffsetY + 'px';
  obj.style.visibility = 'visible';

  return true;
}

function isNumber(str)
{
  return (parseInt(str) + '') == str;
}

function overlibParseParams(arguments)
{
  var argType = 0;

  toolTipCurrentText = '';
  toolTipCurrentWidth = toolTipWidth;
  toolTipCurrentAbove = false;
  toolTipCurrentLeft = false;
  toolTipCurrentMiddle = false;

  for(var i = 0; i < arguments.length; i++)
  {
    if (argType == INARRAY)
    {
      if (isNumber(arguments[i])) toolTipCurrentText = ol_texts[arguments[i]];
      argType = 0;
    }
    else if (argType == WIDTH)
    {
      if (isNumber(arguments[i])) toolTipCurrentWidth = arguments[i];
      argType = 0;
    }
    else
    {
      if (isNumber(arguments[i]))
      {
        argType = arguments[i];
        if (argType == ABOVE) { toolTipCurrentAbove = true; argType = 0; }
        if (argType == LEFT) { toolTipCurrentLeft = true; argType = 0; }
        if (argType == MIDDLE) { toolTipCurrentMiddle = true; argType = 0; }
      }
      else
      {
        if (arguments[i] == 'ABOVE')
        {
          toolTipCurrentAbove = true;
          argType = 0;
        }
        else if (arguments[i] == 'LEFT')
        {
          toolTipCurrentLeft = true;
          argType = 0;
        }
        else if (arguments[i] == 'MIDDLE' || arguments[i] == 'CENTER')
        {
          toolTipCurrentMiddle = true;
          argType = 0;
        }
        else
        {
          toolTipCurrentText = arguments[i];
        }
      }
    }
  }
}

function overlib()
{
  overlibParseParams(arguments);

  document.getElementById('toolTipWidth').style.width = toolTipCurrentWidth + 'px';
  document.getElementById('toolTipShadowWidth').style.width = toolTipCurrentWidth + 'px';

  document.getElementById('toolTipText').innerHTML = toolTipCurrentText;
  document.getElementById('toolTipShadowText').innerHTML = toolTipCurrentText;

  if (toolTipIE)
  {
    document.onmousemove = overlibMouseMove;
  }
  else
  {
    window.onmousemove = overlibMouseMove;
    window.captureEvents(Event.MOUSEMOVE);
  }

  return true;
}
