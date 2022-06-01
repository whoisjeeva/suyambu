"use strict"

const gulp        = require("gulp")
const uglify      = require("gulp-uglify")
const maps        = require("gulp-sourcemaps")
const rename      = require("gulp-rename")
const browserify  = require("browserify")
const Babelify    = require("babelify")
const source      = require("vinyl-source-stream")
const buffer      = require('vinyl-buffer')
const rollupify   = require('rollupify')


gulp.task("compile-background", function() {
    let pipeLine = browserify({
        entries: ["src/background/worker.js"]
    })
    .transform(rollupify, {config: {}}) 
    .transform(Babelify, {presets: ["@babel/preset-env"]})
    .bundle()
    .pipe(source("src/background/worker.js"))
    .pipe(buffer())
    .pipe(maps.init())
    .pipe(rename("worker.min.js"))
    .pipe(uglify())
    .pipe(maps.write(".maps"))
    .pipe(gulp.dest("dist"))
    return pipeLine
})

gulp.task("compile-popup", function() {
    let pipeLine = browserify({
        entries: ["src/popup/app.js"]
    })
    .transform(rollupify, {config: {}}) 
    .transform(Babelify, {presets: ["@babel/preset-env"]})
    .bundle()
    .pipe(source("src/popup/app.js"))
    .pipe(buffer())
    .pipe(maps.init())
    .pipe(rename("app.min.js"))
    .pipe(uglify())
    .pipe(maps.write(".maps"))
    .pipe(gulp.dest("dist"))
    return pipeLine
})

gulp.task("watch", gulp.series("compile-background", "compile-popup", function() {
    gulp.watch(["src/background/*.js", "src/background/**/*.js", "src/engine/*.js", "src/engine/**/*.js"], gulp.series("compile-background"))
    gulp.watch(["src/popup/*.js", "src/popup/**/*.js"], gulp.series("compile-popup"))
}))

gulp.task("default", gulp.series("compile-background", "compile-popup"))
