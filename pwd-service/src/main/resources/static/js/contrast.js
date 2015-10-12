$(document).ready(function () {
    $('#contrastToggle').click(function () {
        $('section').toggleClass('highcontrast');
        $(this).find('span').toggleClass('glyphicon-eye-open');
        $(this).find('span').toggleClass('glyphicon-eye-close');
    });
});