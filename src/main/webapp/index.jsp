<html>
<head>

    <script type="text/javascript" src="staticResources/javascript/jquery-1.8.3.js"></script>

    <script type="text/javascript">
        $(document).ready(function () {

            $("button[name='getMedia']").click(function () {
                var mediaId = $("input[name='mediaId']").val();
                if(mediaId == "" || mediaId == null){
                    alert("mediaId required!");
                    return;
                }

                $.ajax({

                    url:"FileController/downloadMedia",
                    type:"get",
                    data:{
                        "mediaId":mediaId
                    },
                    dataType:"text",
                    success:function (url) {
                        $("div[name='downloadLink']").text(url);
                    }

                });
            });

        });

    </script>
</head>
<body>
<form action="FileController/saveMeida" method="post" enctype="multipart/form-data" target="_blank">
    uploader:<input type="text" name="uploader" />
    <input type="file" name="file" />
    <input type="submit" value="Submit" />
</form>

mediaId:<input type="text"  value="" name="mediaId" /> &nbsp;<button name="getMedia">download</button>
<div name="downloadLink">


</div>

</body>
</html>