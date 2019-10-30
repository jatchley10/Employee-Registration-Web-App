var ProjOne = ( function() {

    return {
        
        getAttendees: function(){
            var that = this;
            let selection = $("select").val();
            let id = selection[selection.length - 1];
            
            $.ajax({
                url: 'registration?id=' + id,
                method: 'GET',
                dataType: 'html',
                success: function(response) {
                    that.showTable(response);                    
                }
            });
        },
        showTable: function(response){
            $("#output").html(response);
        }
    };
}());
