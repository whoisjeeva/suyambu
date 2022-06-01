"use strict"

const gulp        = require("gulp")
const uglify      = require("gulp-uglify")
const babel       = require("gulp-babel")
const concat      = require('gulp-concat')


gulp.task("compile", function() {
    return gulp.src(["./src/hammer.js", "./src/finder.js"])
        .pipe(concat("element_finder.min.js"))
        .pipe(babel({
            presets: ['@babel/env']
        }))
        .pipe(uglify())
        .pipe(gulp.dest("dist"))
})


gulp.task("compile-desktop", function() {
    return gulp.src(["./src/desktop_finder.js"])
        .pipe(concat("element_finder.desktop.min.js"))
        .pipe(babel({
            presets: ['@babel/env']
        }))
        .pipe(uglify())
        .pipe(gulp.dest("dist"))
})


gulp.task("watch", gulp.series("compile", function() {
    gulp.watch(["src/*.js", "src/**/*.js"], gulp.series("compile"))
}))

gulp.task("default", gulp.series("compile", "compile-desktop"))
