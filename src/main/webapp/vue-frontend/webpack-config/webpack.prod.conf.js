'use strict'
const path = require('path')
const utils = require('./utils')
const webpack = require('webpack')
const merge = require('webpack-merge')
const baseWebpackConfig = require('./webpack.base.conf')
const HtmlWebpackPlugin = require('html-webpack-plugin')
const UglifyJsPlugin = require('uglifyjs-webpack-plugin')
const CopyWebpackPlugin = require('copy-webpack-plugin')
const ExtractTextPlugin = require('extract-text-webpack-plugin')

const webpackConfig = merge(baseWebpackConfig, {

    module: {
        rules: utils.styleLoaders({
            sourceMap: true,
            extract: true
        })
    },
    devtool: '#source-map',
    output: {
        // 关键修复：确保所有资源引用都是相对路径
        publicPath: './'
    },
    plugins: [
        // 定义环境变量
        new webpack.DefinePlugin({
            'process.env': {
                NODE_ENV: '"production"'
            }
        }),
        // 使用 UglifyJsPlugin 压缩 JS
        new UglifyJsPlugin({
            uglifyOptions: {
                compress: {
                    warnings: false
                }
            },
            sourceMap: true,
            parallel: true
        }),
        // 提取 CSS 到单独的文件
        new ExtractTextPlugin({
            filename: utils.assetsPath('css/[name].[contenthash].css'),
            allChunks: true,
        }),
        // 生成 index.html 文件
        new HtmlWebpackPlugin({
            filename: 'index.html',
            template: 'index.html',
            inject: true,
            minify: {
                removeComments: true,
                collapseWhitespace: true,
                removeAttributeQuotes: true
            },
            chunksSortMode: 'dependency'
        }),
        // 根据模块的相对路径生成一个四位数的hash作为模块id
        new webpack.HashedModuleIdsPlugin(),
        // 启用作用域提升
        new webpack.optimize.ModuleConcatenationPlugin(),
        // 复制在 .vue 文件之外引用的静态资源
        new CopyWebpackPlugin([
            {
                // 源文件目录：项目根目录下的 static 文件夹
                from: path.resolve(__dirname, '../static'),
                // 目标目录：dist 文件夹下的 static 文件夹
                to: 'static',
                // 忽略点文件
                ignore: ['.*']
            }
        ])
    ]
})

module.exports = webpackConfig
