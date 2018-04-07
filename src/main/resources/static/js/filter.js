
$(".dropdown.companyFilter dt a").on('click', function() {
    $(".dropdown.companyFilter dd ul").slideToggle('fast');
});

$(".dropdown.companyFilter dd ul li a").on('click', function() {
    $(".dropdown.companyFilter dd ul").hide();
});

function getSelectedValue(id) {
    return $("#" + id).find(".companyFilter dt a span.value").html();
}

$(document).bind('click', function(e) {
    var $clicked = $(e.target);
    if (!$clicked.parents().hasClass("companyFilter")) $(".dropdown.companyFilter dd ul").hide();
});

$('.companyFilter .multiSelect input[type="checkbox"]').on('click', function() {

    var company = $(this).closest('.companyFilter .multiSelect').find('.companyFilter input[type="checkbox"]').val(),
        company = $(this).val() + ",";

    if ($(this).is(':checked')) {
        var html = '<span company="' + company + '">' + company + '</span>';
        $('.companyFilter .multiSel').append(html);
        $(".companyFilter .hida").hide();
        $(this).parent().css("background","#31414a");
    } else {
        $('span[company="' + company + '"]').remove();
        var ret = $(".companyFilter .hida");
        $('.companyFilter .dropdown dt a').append(ret);
        if ($('.companyFilter .multiSel').has('span').length == 0) {
            $(".companyFilter .hida").show();
        }
        $(this).parent().css("background","");
    }
});

///////////////////////////////////////////////////////////

$(".dropdown.locationFilter dt a").on('click', function() {
    $(".dropdown.locationFilter dd ul").slideToggle('fast');
});

$(".dropdown.locationFilter dd ul li a").on('click', function() {
    $(".dropdown.locationFilter dd ul").hide();
});

function getSelectedValue(id) {
    return $("#" + id).find(".locationFilter dt a span.value").html();
}

$(document).bind('click', function(e) {
    var $clicked = $(e.target);
    if (!$clicked.parents().hasClass("locationFilter")) $(".dropdown.locationFilter dd ul").hide();
});

$('.locationFilter .multiSelect input[type="checkbox"]').on('click', function() {

    var location = $(this).closest('.locationFilter .multiSelect').find('.locationFilter input[type="checkbox"]').val(),
        location = $(this).val() + ",";

    if ($(this).is(':checked')) {
        var html = '<span location="' + location + '">' + location + '</span>';
        $('.locationFilter .multiSel').append(html);
        $(".locationFilter .hida").hide();
        $(this).parent().css("background","#31414a");
    } else {
        $('span[location="' + location + '"]').remove();
        var ret = $(".locationFilter .hida");
        $('.locationFilter .dropdown dt a').append(ret);
        if ($('.locationFilter .multiSel').has('span').length == 0) {
            $(".locationFilter .hida").show();
        }
        $(this).parent().css("background","");
    }
});

///////////////////////////////////////////////////////////

$(".dropdown.typeFilter dt a").on('click', function() {
    $(".dropdown.typeFilter dd ul").slideToggle('fast');
});

$(".dropdown.typeFilter dd ul li a").on('click', function() {
    $(".dropdown.typeFilter dd ul").hide();
});

function getSelectedValue(id) {
    return $("#" + id).find(".typeFilter dt a span.value").html();
}

$(document).bind('click', function(e) {
    var $clicked = $(e.target);
    if (!$clicked.parents().hasClass("typeFilter")) $(".dropdown.typeFilter dd ul").hide();
});

$('.typeFilter .multiSelect input[type="checkbox"]').on('click', function() {

    var type = $(this).closest('.typeFilter .multiSelect').find('.typeFilter input[type="checkbox"]').val(),
        type = $(this).val() + ",";

    if ($(this).is(':checked')) {
        var html = '<span type="' + type + '">' + type + '</span>';
        $('.typeFilter .multiSel').append(html);
        $(".typeFilter .hida").hide();
        $(this).parent().css("background","#31414a");
    } else {
        $('span[type="' + type + '"]').remove();
        var ret = $(".typeFilter .hida");
        $('.typeFilter .dropdown dt a').append(ret);
        if ($('.typeFilter .multiSel').has('span').length == 0) {
            $(".typeFilter .hida").show();
        }
        $(this).parent().css("background","");
    }
});




function multiSearch(input) {
    // Declare variables
    var filter, ul, li, i;
    filter = input.value.toUpperCase();
    ul = input.parentNode;
    li = ul.getElementsByTagName('li');

    // Loop through all list items, and hide those who don't match the search query
    for (i = 0; i < li.length; i++) {
        var index = li[i].textContent.toUpperCase().indexOf(filter);
        if (index === 0) {
            li[i].style.display = "";
        } else {
            li[i].style.display = "none";
        }
    }
}

$("div .multiSelect ul li").on('click', function() {
    $(this).children('input')[0].click();
});