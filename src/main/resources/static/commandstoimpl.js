Search "gcomm.SendCommand" (60 hits in 1 file)
  C:\Documents and Settings\BBB\My Documents\cqserver\cq\modificated\orig\client\code\syscode\fla29.as (60 hits)
	Line 1393:                 gcomm.SendCommand("CONNCHECK", "");
	Line 1531:         gcomm.SendCommand("WEBLOGIN", "USERID=\"" + gcomm.webloginid + "\" SID=\"" + gcomm.webloginsess + "\" WHID=\"" + gcomm.waithallid + "\"" + __reg1 + __reg2);
	Line 1534:     gcomm.SendCommand("LOGIN", "USER=\"" + gcomm.loginuser + "\" PASS=\"" + gcomm.loginpass + "\" WHID=\"" + gcomm.waithallid + "\"" + __reg2);
	Line 1577:         gcomm.SendCommand("READY", "");
	Line 1616: gcomm.SendCommand = function (cmd, params)
	Line 1812:                     gcomm.SendCommand("KEYRESPONSE", "K=\"" + InitNet(__reg7.E, __reg7.N) + "\"");
	Line 2757:     gcomm.SendCommand("GETDATA", " QUERY=\"" + __reg1 + "\"");
	Line 2811:         gcomm.SendCommand("LOGOUT", "");
	Line 2857:     gcomm.SendCommand("CHANGEWAITHALL", "WAITHALL=\"" + awhnum + "\"");
	Line 3092:     gcomm.SendCommand("CHATCOLOR", "COLOR=\"" + acolor + "\"");
	Line 3193:     gcomm.SendCommand("ORDERSERVICE", "SERVICE=\"" + aservice + "\"");
	Line 3256:     gcomm.SendCommand("TOURNAMENTREG", "");
	Line 4448:         gcomm.SendCommand("CHATMSG", "MSG=\"" + __reg1 + "\"");
	Line 4465:     gcomm.SendCommand("CHATCLOSE", "ID=\"" + anum + "\" ACTIVECHAT=\"" + __reg1.id + "\" MSTATE=\"" + __reg1.lastmsgstate + "\"");
	Line 4541:     gcomm.SendCommand("SETACTIVECHAT", "ID=\"" + cr.id + "\" MSTATE=\"" + cr.lastmsgstate + "\"");
	Line 5200:     gcomm.SendCommand("ADDSEPROOM", "MAP=\"" + __reg6 + "\" OPP1=\"" + __reg3.opp1name + "\" OPP2=\"" + __reg3.opp2name + "\"" + " OOPP=\"" + __reg4 + "\"" + " RULES=\"" + __reg5 + "\"" + " SEPMESSAGEID=\"" + __reg7 + "\"" + " QCATS=\"" + __reg2 + "\"" + " SUBRULES=\"" + __reg3.SUBRULES.selectedIndex + "\"");
	Line 5518:     gcomm.SendCommand("ENTERROOM", "SEPROOM=\"" + aroom.roomid + "\"");
	Line 5523:     gcomm.SendCommand("DENYSEPROOM", "SEPROOM=\"" + roomid + "\"");
	Line 5584:         gcomm.SendCommand("EXITROOM", "");
	Line 5626:         gcomm.SendCommand("EXITROOM", "");
	Line 6470:     gcomm.SendCommand("SETMAPPATTERN", "INDEX=\"" + NumberVal(gsys.mypremium.map_pattern.index) + "\"");
	Line 6821:                             gcomm.SendCommand("GETUSERINFO", "USER=\"" + __reg6[0] + "\"");
	Line 6946:         gcomm.SendCommand("CHATMSG", "MSG=\"" + atext + "\"");
	Line 7402:     gcomm.SendCommand("STOPCHAT", "NOCHAT=\"1\" TUID=\"" + gwhall.userinfotag.ID + "\"" + " MODE=\"1\"" + " MINUTES=\"" + minutes.toString() + "\"" + " REASON=\"" + gwhm.CHATMODWIN.chatreasonnum + "\"");
	Line 7408:     gcomm.SendCommand("STOPCHAT", "NOCHAT=\"1\"TUID=\"" + gwhall.userinfotag.ID + "\"" + " MODE=\"3\"" + " GAMES=\"" + games.toString() + "\"" + " REASON=\"" + gwhm.CHATMODWIN.chatreasonnum + "\"");
	Line 7414:     gcomm.SendCommand("STOPCHAT", "NOCHAT=\"0\" TUID=\"" + gwhall.userinfotag.ID + "\"");
	Line 7882:     gcomm.SendCommand("GETUSERINFO", "USER=\"" + uname + "\"");
	Line 7889:         gcomm.SendCommand("EXITROOM", "");
	Line 7907:         gcomm.SendCommand("MODFRIENDLIST", "IGNORED=\"+" + __reg1 + "\"");
	Line 7912:         gcomm.SendCommand("MODFRIENDLIST", "FRIENDS=\"+" + __reg1 + "\"");
	Line 7916:         gcomm.SendCommand("MODFRIENDLIST", "FRIENDS=\"-" + __reg1 + "\"");
	Line 7920:         gcomm.SendCommand("MODFRIENDLIST", "IGNORED=\"-" + __reg1 + "\"");
	Line 7938:         gcomm.SendCommand("SETCHATFLAGS", "SETFLAG=\"1\"");
	Line 7941:     gcomm.SendCommand("SETCHATFLAGS", "CLEARFLAG=\"1\"");
	Line 7946:     gcomm.SendCommand("CONFIRM", "");
	Line 7957:     gcomm.SendCommand("ENTERROOM", "ROOM=\"" + roomid + "\"");
	Line 7964:     gcomm.SendCommand("EXITROOM", "");
	Line 7970:     gcomm.SendCommand("STARTROBOT", "");
	Line 7982:     gcomm.SendCommand("CHATADDUSER", "CHATID=\"" + gwhall.activechat + "\" USER=\"" + uname + "\"");
	Line 8002:     gcomm.SendCommand("CHATADDUSER", "CHATID=\"-1\" USER=\"" + uname + "\"");
	Line 8047:     gcomm.SendCommand("USERSEARCH", "AGEFROM=\"" + __reg1.AGEFROMM.AGEFROM + "\" AGETO=\"" + __reg1.AGETOM.AGETO + "\" SEX=\"" + (convNumber(gwhall.search_sex) - 1) + "\"" + " LOCATION=\"" + __reg1.LOCATION.getSelectedItem().data + "\"");
	Line 8074:     gcomm.SendCommand("SETDATA", "SINFO=\"" + escape(__reg1.SHORTINFO) + "\"" + " FACE=\"" + convNumber(gsys.mydatatag.FACE) + "\"");
	Line 8254:     gcomm.SendCommand("SETDATA", "CHATCOLOR=\"" + acolor + "\"");
	Line 8839:     gcomm.SendCommand("CHANGEWAITHALL", "WAITHALL=\"1\"");
	Line 9709:         gcomm.SendCommand("TIP", "TIP=\"" + __reg3 + "\"" + __reg4);
	Line 11665:     gcomm.SendCommand("REPORT", "USER=\"" + game["MYOPP" + __reg1.pnum] + "\"");
	Line 11685:         gcomm.SendCommand("MESSAGE", "TO=\"" + target + "\" TEXT=\"" + __reg1 + "\"");
	Line 11711:     gcomm.SendCommand("SELECT", "AREA=\"" + convNumber(anum) + "\"");
	Line 11740:     gcomm.SendCommand("ANSWER", "ANSWER=\"" + ans + "\"");
	Line 11759:     gcomm.SendCommand("HELP", "HELP=\"" + helpid + "\"");
	Line 11779:             gcomm.SendCommand("CLOSEGAME", "");
	Line 11804:     gcomm.SendCommand("LOGOUT", "");
	Line 11810:     gcomm.SendCommand("BADQMARK", "");
	Line 11818:     gcomm.SendCommand("RATEQUESTION", "RATE=\"" + game.myquestionrate + "\"");
	Line 12059:     gcomm.SendCommand("SELECTMH", "AREA=\"" + __reg1 + "\" TYPE=\"" + __reg2 + "\"");
	Line 12071:     gcomm.SendCommand("SELECTMH", "AREA=\"" + gmap.MHSUBJSELWIN.selectedarea + "\" TYPE=\"2\" CATID=\"" + __reg1 + "\"");
	Line 15094:                     gcomm.SendCommand("LOGOUT", "");
	Line 15400:         gcomm.SendCommand("SELECTROW", "ROW=\"" + rownum + "\"");
	Line 15468:     gcomm.SendCommand("SELECTCARD", "CARD=\"" + __reg3 + "\"");
	Line 15485:         gcomm.SendCommand("MESSAGE", "TO=\"0\" TEXT=\"" + __reg1 + "\"");
