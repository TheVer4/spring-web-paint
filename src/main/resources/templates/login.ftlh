<#import "parts/common.ftl" as c>
<#import "parts/loginForm.ftl" as lf>
<@c.page>
Авторизация
<@lf.login "/login"/>
<a href="/registration">Зарегистрироваться</a>
</@c.page>