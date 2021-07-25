<#-- @ftlvariable name="entries" type="kotlin.collections.List<com.jetbrains.handson.website.TimeEntry>" -->
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Sim training Journal</title>
</head>
<body style="text-align: center; font-family: sans-serif">
<img src="/static/ktor.png">
<h1>Training Journal </h1>
<p><i>Powered by Ktor, kotlinx.html & Freemarker!</i></p>
<hr>
<#list entries as item>
    <div>
        <p>${item.id}</p>
        <p>${item.timeOfEntry}</p>
        <p>${item.measuredTime}</p>
        <p>item.problems</p>
    </div>
</#list>
<hr>
<div>
    <h3>Add a new time entry!</h3>
    <form action="/submit" method="post">
        <label for="timeOfEntry">Time of entry</label>
        <input type="text" name="timeOfEntry">
        <br>
        <label for="measuredTime">Measured time</label>
        <input type="text" name="measuredTime">
        <br>
        <label for="problems">Problems if any</label>
        <textarea name="problems"></textarea>
        <br>
        <input type="submit">
    </form>
</div>
</body>
</html>
