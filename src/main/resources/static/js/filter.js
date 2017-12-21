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

$('.companyFilter .mutliSelect input[type="checkbox"]').on('click', function() {

    var company = $(this).closest('.companyFilter .mutliSelect').find('.companyFilter input[type="checkbox"]').val(),
        company = $(this).val() + ",";

    if ($(this).is(':checked')) {
        var html = '<span company="' + company + '">' + company + '</span>';
        $('.companyFilter .multiSel').append(html);
        $(".companyFilter .hida").hide();
    } else {
        $('span[company="' + company + '"]').remove();
        var ret = $(".companyFilter .hida");
        $('.companyFilter .dropdown dt a').append(ret);
        if ($('.companyFilter .multiSel').has('span').length == 0) {
            $(".companyFilter .hida").show();
        }
    }
});

///////////////////////////////////////////////////////////

$(".dropdown.cityFilter dt a").on('click', function() {
    $(".dropdown.cityFilter dd ul").slideToggle('fast');
});

$(".dropdown.cityFilter dd ul li a").on('click', function() {
    $(".dropdown.cityFilter dd ul").hide();
});

function getSelectedValue(id) {
    return $("#" + id).find(".cityFilter dt a span.value").html();
}

$(document).bind('click', function(e) {
    var $clicked = $(e.target);
    if (!$clicked.parents().hasClass("cityFilter")) $(".dropdown.cityFilter dd ul").hide();
});

$('.cityFilter .mutliSelect input[type="checkbox"]').on('click', function() {

    var city = $(this).closest('.cityFilter .mutliSelect').find('.cityFilter input[type="checkbox"]').val(),
        city = $(this).val() + ",";

    if ($(this).is(':checked')) {
        var html = '<span city="' + city + '">' + city + '</span>';
        $('.cityFilter .multiSel').append(html);
        $(".cityFilter .hida").hide();
    } else {
        $('span[city="' + city + '"]').remove();
        var ret = $(".cityFilter .hida");
        $('.cityFilter .dropdown dt a').append(ret);
        if ($('.cityFilter .multiSel').has('span').length == 0) {
            $(".cityFilter .hida").show();
        }
    }
});

///////////////////////////////////////////////////////////

$(".dropdown.languageFilter dt a").on('click', function() {
    $(".dropdown.languageFilter dd ul").slideToggle('fast');
});

$(".dropdown.languageFilter dd ul li a").on('click', function() {
    $(".dropdown.languageFilter dd ul").hide();
});

function getSelectedValue(id) {
    return $("#" + id).find(".languageFilter dt a span.value").html();
}

$(document).bind('click', function(e) {
    var $clicked = $(e.target);
    if (!$clicked.parents().hasClass("languageFilter")) $(".dropdown.languageFilter dd ul").hide();
});

$('.languageFilter .mutliSelect input[type="checkbox"]').on('click', function() {

    var language = $(this).closest('.languageFilter .mutliSelect').find('.languageFilter input[type="checkbox"]').val(),
        language = $(this).val() + ",";

    if ($(this).is(':checked')) {
        var html = '<span language="' + language + '">' + language + '</span>';
        $('.languageFilter .multiSel').append(html);
        $(".languageFilter .hida").hide();
    } else {
        $('span[language="' + language + '"]').remove();
        var ret = $(".languageFilter .hida");
        $('.languageFilter .dropdown dt a').append(ret);
        if ($('.languageFilter .multiSel').has('span').length == 0) {
            $(".languageFilter .hida").show();
        }
    }
});