<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Freemarker测试</title>
</head>
<body>
<#--freemarker测试-->

<h1>${name}；${message}</h1>

<br>
<hr>
<br>

<#--assign-->
<#--简单类型-->
<#assign  name="JJ"/>
名字：${name}
<br>

<br>
<hr>
<br>
<#--对象-->
<#assign linkMan={"mobie":"138000000","address":"广州天河"}/>
联系电话：${linkMan.mobie},联系地址:${linkMan.address}

<br>
<hr>
<br>
include测试<br>
<#--include-->
<#include "header.ftl"/>
<br>

<br>
<hr>
<br>
if测试<br>
<#assign bool=false/>
<#if bool>
    bool的值为true
<#else>
    bool的值为false
</#if>

<br>
<hr>
<br>
list测试<br>
<#list goodsList as good>
    索引：${good_index},<br>
    名字：${good.name},<br>
    价格：${good.price}<br>
</#list>
一共${goodsList?size}条数据

<br>
<hr>
<br>
eval将字符串转换为json对象<br>
<#assign str='{"id":123,"text":"itcast"}'>

<#assign jsonObj=str?eval>
id为:${jsonObj.id},text为：${jsonObj.text}
<br>

<br>
<hr>
<br>
日期格式处理<br>
当前日期:${today?date}<br>
当前时间:${today?time}<br>
当前日期+时间:${today?datetime}<br>
格式化显示当前日期时间:${today?string('yyyy年MM月dd日 HH:mm:ss')}<br>

<br>
<hr>
<br>
数字转换为字符串<br>
数字number=${number}; 字符串number?c=${number?c}<br>

<br>
<hr>
<br>
空值处理<br>
<#assign abc=false>

${abc???string} <#--判断abc变量是否存在，存在返回 true  这里上面已经定义abc为false值 所以存在-->
<br>            <#--否则false，然后对返回的值调用其内置函数 string-->

${str!"str空值的默认显示值"}<br> <#--这里str1不存在 显示默认显示值-->
                                <#--如果str存在 则输出str的值-->
<#--判断变量是否存在-->
<#if str1??><#--表示去判断 str 变量是否存在，存在则 true，不存在为 false -->
    str变量存在 <#--存在输出这个-->
<#else >
    str变量不存在 <#--不存在输出这个-->
</#if>

</body>
</html>