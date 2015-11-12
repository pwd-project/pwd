$(document).ready(function() {
		
		$('[data-toggle="tooltip"]').tooltip(); 
		$(window).on("keydown", this, function (event) {
			if (event.keyCode == 116) {
				Cookies.remove('style');
			}
		});
		var plus = 0;
		$('.font-basic').click(function() {
			$('p, span, h1, h2, h3, h4, h5:not(".rank-value"), h6, ul, li, td, th').removeAttr('style');
			plus=0;
		});
		$('.font-plus').click(function() {
			$('p, span, h1, h2, h3, h4, h5:not(".rank-value"), h6, ul, li, td, th').removeAttr('style');
			$('p, .section span, h1, h2, h3, h4, h5:not(".rank-value"), h6, ul, li, td, th').css("font-size", function() {
				plus=plus+1;
				return parseInt($(this).css('font-size')) * 1.5 + 'px';		
			});
		});
		$('.font-plus-plus').click(function() {
			$('p,  span, h1, h2, h3, h4, h5:not(".rank-value"), h6, ul, li, td, th').removeAttr('style');
			$('p, .section span, h1, h2, h3, h4, h5:not(".rank-value"), h6, ul, li, td, th').css("font-size", function() {
				
				return parseInt($(this).css('font-size')) * 2 + 'px';		
			});
		
		});
		$('.contrast-1').click(function() {
			$('.rank-ico').attr('src','/img/rank.png');
			$('.visible-regular').show();
			$('.visible-contrast').hide();
			$('#contrast-style').remove();
			Cookies.set('style', '1');
		});
		$('.contrast-2').click(function() {
			$('.rank-ico').attr('src','/img/rank-white.png');
			$('.visible-contrast').show();
			$('.visible-regular').hide();
			$('#contrast-style').remove();
			$('head').append('<link id="contrast-style" rel="stylesheet" href="/css/contrast-2.css" type="text/css" />');
			Cookies.set('style', '2');
		});
		$('.contrast-3').click(function() {
			$('.rank-ico').attr('src','/img/rank.png');
			$('.visible-contrast').show();
			$('.visible-regular').hide();
			$('#contrast-style').remove();
			$('head').append('<link id="contrast-style" rel="stylesheet" href="/css/contrast-3.css" type="text/css" />');
			Cookies.set('style', '3');
		});
		$('.contrast-4').click(function() {
			$('.rank-ico').attr('src','/img/rank-yellow.png');
			$('.visible-contrast').show();
			$('.visible-regular').hide();
			$('#contrast-style').remove();
			$('head').append('<link id="contrast-style" rel="stylesheet" href="/css/contrast-4.css" type="text/css" />');
			Cookies.set('style', '4');
		});
		
		var style = Cookies.get('style');
		if(style == 1){
			$('#contrast-style').remove();
			$('.rank-ico').attr('src','/img/rank.png');
		}
		else if(style == 2){
			$('.rank-ico').attr('src','/img/rank-white.png');
			$('head').append('<link id="contrast-style" rel="stylesheet" href="/css/contrast-'+style+'.css" type="text/css" />');
		}
		else if(style == 3){
			$('.rank-ico').attr('src','/img/rank.png');
			$('head').append('<link id="contrast-style" rel="stylesheet" href="/css/contrast-'+style+'.css" type="text/css" />');
		}
		else if(style == 4){
			$('.rank-ico').attr('src','/img/rank-yellow.png');
			$('head').append('<link id="contrast-style" rel="stylesheet" href="/css/contrast-'+style+'.css" type="text/css" />');
		}
		
		
	});

		