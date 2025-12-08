(function(document, $) {
    "use strict";

    $(document).on("click", "#cf-submit", function() {
        var name = $("#cf-name").val();
        var email = $("#cf-email").val();
        var mobile = $("#cf-mobile").val();
        var nodePath = $("#cf-nodePath").val(); // present if edit

        var data = {
            name: name,
            email: email,
            mobile: mobile
        };

        if (nodePath) {
            data.nodePath = nodePath;   // for update
            data.action = "update";
        } else {
            data.action = "create";
        }

        $.ajax({
            type: "POST",
            url: "/bin/contact/formdata",
            data: data,
            success: function(response) {
                alert("Saved successfully!");
                // optional: reload page or clear form
                location.reload();
            },
            error: function(xhr) {
                alert("Error saving data: " + xhr.status);
            }
        });
    });

    // Delete handler (for table rows)
    $(document).on("click", ".contact-delete", function() {
        var path = $(this).data("path");

        if (!confirm("Are you sure you want to delete this contact?")) {
            return;
        }

        $.ajax({
            type: "POST",
            url: "/bin/contact/formdata",
            data: {
                action: "delete",
                deletePath: path
            },
            success: function() {
                alert("Deleted successfully!");
                location.reload();
            },
            error: function() {
                alert("Error deleting contact");
            }
        });
    });

    // Edit handler (fills form from table row)
    $(document).on("click", ".contact-edit", function() {
        var name = $(this).data("name");
        var email = $(this).data("email");
        var mobile = $(this).data("mobile");
        var path = $(this).data("path");

        $("#cf-name").val(name);
        $("#cf-email").val(email);
        $("#cf-mobile").val(mobile);
        $("#cf-nodePath").val(path); // to tell servlet it's an update
    });

})(document, Granite.$);
