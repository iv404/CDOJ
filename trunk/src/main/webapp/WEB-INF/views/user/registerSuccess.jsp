<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/page" prefix="page" %>
<%@ taglib prefix="cdoj" uri="/WEB-INF/cdoj.tld" %>
<%--
~ /*
~  * cdoj, UESTC ACMICPC Online Judge
~  * Copyright (c) 2013 fish <@link lyhypacm@gmail.com>,
~  * 	mzry1992 <@link muziriyun@gmail.com>
~  *
~  * This program is free software; you can redistribute it and/or
~  * modify it under the terms of the GNU General Public License
~  * as published by the Free Software Foundation; either version 2
~  * of the License, or (at your option) any later version.
~  *
~  * This program is distributed in the hope that it will be useful,
~  * but WITHOUT ANY WARRANTY; without even the implied warranty of
~  * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
~  * GNU General Public License for more details.
~  *
~  * You should have received a copy of the GNU General Public License
~  * along with this program; if not, write to the Free Software
~  * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
~  */
--%>

<%--
  Created by IntelliJ IDEA.
  User: mzry1992
  Date: 13-1-22
  Time: 下午2:19
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title></title>
</head>
<body>
    <s:div cssClass="row">
        <s:div cssClass="span12">
            <p>User name : |<s:property value="userDTO.userName"/>|</p>
            <p>Password : |<s:property value="userDTO.password"/>|</p>
            <p>Repeat password : |<s:property value="userDTO.passwordRepeat"/>|</p>
            <p>Nick name : |<s:property value="userDTO.nickName"/>|</p>
            <p>Email : |<s:property value="userDTO.email"/>|</p>
            <p>School : |<s:property value="userDTO.school"/>|</p>
            <p>Department : |<s:property value="userDTO.department"/>|</p>
            <p>Student ID : |<s:property value="userDTO.studentId"/>|</p>
        </s:div>
    </s:div>
</body>
</html>