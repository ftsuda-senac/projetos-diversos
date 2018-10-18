/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

$(document).ready(function() {
    $("a.remover").click(function(ev) {
        ev.preventDefault();
        var id = $(this).attr("rel");
        var remover = confirm("Deseja remover o veiculo?");
        if (remover) {
            $("#hdnId").val(id);
            $("#frmRemover").submit();
        }
    });
});
