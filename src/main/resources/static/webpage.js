angular.module('htmlParserApp', [ 'ngResource', 'jsonFormatter' ]).controller(
		'ParserController', function($resource, $scope) {
			let
			controller = this;
			controller.message = "Start Analysis";
			controller.webpage = {};
			let
			WebPage = $resource('/webpages/:resourceId', {
				resourceId : '@resourceId'
			});
			$scope.startAnalysis = function() {
				controller.message = "Processing";
				WebPage.get({
					resourceId : btoa(controller.link)
				}, function(webpage) {
					controller.message = "Start Analysis";
					controller.webpage = webpage;
				});
			};

		});