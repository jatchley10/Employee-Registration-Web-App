var reg = ( function() {

    return {
       genFullName: function(){
           var fullName = "";
           fullName += $("#fName").val() + " ";
           fullName += $("#lName").val();
           $("#disName").val(fullName);        
       },
       
       postEmps : function(){
           var fName = $("#fName").val();
           var lName = $("#lName").val();
           var disName = $("#disName").val();
           var course = $("select").val();
           var that = this;
           var data = fName + ";" + lName + ";" + disName + ";" + course[course.length - 1];
           $.ajax({
                url: 'registration?data='+data,
                method: 'POST',
                dataType: 'json',
                success: function(response) {
                    that.generateSuccess(response);                   
                }
            });
       },
       generateSuccess: function(response){
           var name = response.name;
           var id = response.regID;
           var text = "Congratulations! You have successfully registered as: " + name;
           text += "</br></br>Your registration code is: <b>"+id+"</b>";
           $("#output").html(text);
       }
    };
}());