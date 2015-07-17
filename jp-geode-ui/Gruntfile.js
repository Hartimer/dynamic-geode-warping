/* jshint node:true, unused: true */
module.exports = function (grunt) {
    var path = require('path');

    grunt.initConfig({

        projectName: path.basename(__dirname),

        execute: {
            geodeUi: {
                src: ['geode-ui.js']
            }
        },

        sass: {
            dist: {
                files: {
                    'webapp/public/css/style.css' : 'webapp/styles/style.scss',
                    'webapp/public/css/c3.css' : 'webapp/styles/c3.scss'
                }
            }
        },

        watch: {
            styles: {
                files: [ 'webapp/styles/**/*.*' ],
                tasks: 'sass',
            }
        },

    });

    grunt.registerTask('default', [
        'sass',
        'execute:geodeUi'
    ]);

    grunt.loadNpmTasks('grunt-run');
    grunt.loadNpmTasks('grunt-execute');
    grunt.loadNpmTasks('grunt-contrib-watch');
    grunt.loadNpmTasks('grunt-sass');

};
