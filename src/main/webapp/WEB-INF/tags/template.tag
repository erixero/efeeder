<%@tag description="Page template" pageEncoding="UTF-8" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@attribute name="javascript" fragment="true" %>

<!DOCTYPE html>
<html lang="en">
	<head>
		<meta charset="utf-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<meta name="viewport" content="width=device-width, initial-scale=1.0">

		<!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
		<meta name="description" content="">
		<meta name="author" content="">

		<title>Efeeder</title>

		<link href="/assets/css/select2.min.css" rel="stylesheet">
		<link href="/assets/css/app.css" rel="stylesheet">
		<link href="/assets/css/foodMeetings.css" rel="stylesheet">
		<link href="/assets/css/foodMeetingInfo.css" rel="stylesheet">
		<link rel="stylesheet" href="/assets/css/materialize.css">
		<link rel="stylesheet" href="/assets/css/materialize.clockpicker.css">
		<link rel="stylesheet" href="/assets/css/place.css">
		<link rel="stylesheet" href="/assets/css/imageUpload.css">
		<link rel="stylesheet" href="/assets/css/nouislider.css">
		<link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons">
	</head>

	<body>

		<t:header/>

		<div class="container" >
			<jsp:doBody/>
		</div>

		<!--<t:footer/>-->

		<script src="/assets/js/lib/jquery.js"></script>
		<script src="/assets/js/lib/js.cookie.js"></script>
		<script src="/assets/js/lib/bootstrap.js"></script>
		<script src="/assets/js/lib/datatables.min.js"></script>
		<script src="/assets/js/lib/lodash.js"></script>
		<script src="/assets/js/lib/select2.js"></script>
		<script src="/assets/js/lib/jsrender.js"></script>
		<script src="/assets/js/lib/masonry.min.js"></script>
		<script src="/assets/js/lib/isotope.pkgd.min.js"></script>
		<script src="/assets/js/lib/packery-mode.pkgd.min.js"></script>
		<script src="/assets/js/lib/imagesLoaded.min.js"></script>
		<script src="/assets/js/lib/moment.min.js"></script>
		<script src="/assets/js/lib/materialize.js"></script>
		<script src="/assets/js/lib/materialize.clockpicker.js"></script>
		<script src="/assets/js/lib/jquery.validate.js"></script>
		<script src="/assets/js/lib/jquery.uploadPreview.min.js"></script>
		<script type="text/javascript" src="/assets/js/lib/jquery.countdown.js"></script>
		<script src="/assets/js/lib/nouislider.min.js"></script>
		<script src="/assets/js/communication-service.js"></script>
		<script src="/assets/js/formPlaceValidation.js"></script>
		<script src="/assets/js/mainScript.js"></script>
		<jsp:invoke fragment="javascript"/>

	</body>
</html>
