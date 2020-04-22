<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib  prefix="c"  uri="http://java.sun.com/jsp/jstl/core" %>    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>User</title>
</head>
<body>
    <h2>User:등록</h2>
    <hr/>
    <form action="select_one.do" method="get">
           이름:<input type="text" name="name" size="15"><br/>
           성별:<input type="text" name="sex" size="15"><br/>
           전화:<input type="text" name="tel" size="15"><br/>
           나이:<input type="text" name="age" size="15"><br/>
           핸드폰:<input type="text" name="cellPhone" size="15"><br/>
        <input type="submit" value="전송">
    </form>
    <hr/>
    
이름: ${vo.name}<br/>
성별: ${vo.sex}<br/>
전화: ${vo.tel}<br/>
나이: ${vo.age}<br/>
핸드폰: ${vo.cellPhone}<br/>       
   
  
    
    
    
</body>
</html>



























