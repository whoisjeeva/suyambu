"use strict"

const gulp        = require("gulp")
const uglify      = require("gulp-uglify")
const babel       = require("gulp-babel")
const maps        = require("gulp-sourcemaps")
const rename      = require("gulp-rename")
const include     = require('gulp-include')
const browserify  = require("browserify")
const Babelify    = require("babelify")
const source      = require("vinyl-source-stream")
const buffer      = require('vinyl-buffer')
const rollupify   = require('rollupify')
const concat      = require('gulp-concat')
const sass        = require("gulp-sass")(require("sass"))


const dirs = {
    libs: [
        "./libs1/blockly_compressed.js", 
        "./libs1/blocks_compressed.js", 
        "./libs1/javascript_compressed.js", 
        "./libs1/dom.min.js", 
        "./libs1/en.js"
    ],
    src: [
        "./js/toolbox.js", 
        "./js/custom_blocks.js", 
        "./js/editor.js", 
        "./js/reset.js"
    ],
    dest: "dist"
}

gulp.task("compile-libs", function() {
    return gulp.src(dirs.libs)
        .pipe(concat("bundle.min.js"))
        .pipe(gulp.dest(dirs.dest))
})

gulp.task("compile-src", function() {
    return gulp.src(dirs.src)
        .pipe(concat("editor.min.js"))
        .pipe(babel({
            presets: ['@babel/env']
        }))
        .pipe(maps.init())
        .pipe(uglify())
        .pipe(maps.write(".maps"))
        .pipe(gulp.dest(dirs.dest))
})


gulp.task("watch", gulp.series("compile-libs", "compile-src", function() {
    gulp.watch(["js/*.js", "js/**/*.js"], gulp.series("compile-src"))
    gulp.watch(["libs1/*.js", "libs1/**/*.js"], gulp.series("compile-libs"))
}))

gulp.task("default", gulp.series("compile-libs", "compile-src"))
