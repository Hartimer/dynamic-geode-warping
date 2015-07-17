/* global angular, io, c3 */

(function () { 'use strict';

var MAX_GRAPH_POINTS = 100;
var NUM_EVENTS_RETAINED = 5;

var xData = [ 'x' ];
var series1Data = [ 'timeseries' ];
var chart;

//
// Socket.IO configuration
//
var socket = io.connect('http://localhost:3000');

socket.on('dataPoint', function (dataPoint) {

    xData.push(dataPoint.t);
    series1Data.push(dataPoint.value);
    if (xData.length > MAX_GRAPH_POINTS) {
        xData.splice(1, 1);
        series1Data.splice(1, 1);
    }

    if (chart) {
        chart.load({
            columns: [ xData, series1Data ]
        });
    }

});


//
// Angular app
//
angular.module('net.jp.geode-ui', [ ])


.directive('jpGeodeTsEvent', ['$timeout', function ($timeout) {
    return {
        require: ['?ngModel'],
        restrict: 'E',
        scope: {
          ngModel: '=ngModel'
        },
        template:
        '<div class="ts-event-container">' +
            '<div class="ts-event-chart">' +
            '</div>' +
            '<span class="ts-event-count">{{ model.count }}</span>' +
        '</div>',
        link: function ($scope, $elem) {
            var xvals = [ 'x' ];
            var yvals = [ 'pattern' ];
            var cols = [ xvals, yvals ];

            $scope.model = {
                count: 0,
            };

            if ($scope.ngModel) {
                $scope.model.count = $scope.ngModel.count;
                for (var i = 0; i < $scope.ngModel.metrics.length; i++) {
                    xvals.push($scope.ngModel.metrics[i].t);
                    yvals.push($scope.ngModel.metrics[i].value);
                }
            }

            $scope.chart = c3.generate({
                bindto: $elem.find('.ts-event-chart')[0],
                data: {
                  x: 'x',
                  columns: cols,
                  colors: {
                    pattern: '#007d00'
                  }
                },
                tooltip: {
                  show: false
                },
                axis: {
                  x: {
                      type: 'timeseries',
                      // tick: {
                      //     format: '%Y-%m-%d'
                      // }
                  }
                }
            });
        }
    };
}])


.controller('GeodeUiCtrl', [
         '$scope','$location',
function ($scope , $location ) {

    //
    // Chart configuration
    //
    chart = c3.generate({
        bindto: '#chart',
        data: {
          x: 'x',
          columns: [ xData, series1Data ]
        },
        transition: {
          duration: 0
        },
        tooltip: {
          show: false
        },
        point: {
          show: false
        },
        axis: {
          x: {
              type: 'timeseries',
              count: 10,
              // tick: {
              //     format: '%Y-%m-%d'
              // }
          },
          y: {
              min: 0,
              max: 190,
          }
        }
    });

    $scope.model = {
        tsEvents: []
    };

    socket.on('tsEvent', function (tsEvent) {

        $scope.model.tsEvents.unshift(tsEvent);
        if ($scope.model.tsEvents.length >= NUM_EVENTS_RETAINED) {
            $scope.model.tsEvents.splice(-1, 1);
            $scope.model.tsEvents.length = NUM_EVENTS_RETAINED;
        }

        $scope.$apply();
    });

}])


;


})();

