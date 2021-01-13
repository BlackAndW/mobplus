const path = require('path');
const webpack = require('webpack');
const createThemeColorReplacerPlugin = require('./config/plugin.config');

function resolve (dir) {
    return path.join(__dirname, dir);
}

// vue.config.js
const vueConfig = {
    configureWebpack: {
        plugins: [
            // Ignore all locale files of moment.js
            new webpack.IgnorePlugin(/^\.\/locale$/, /moment$/),
            createThemeColorReplacerPlugin()
        ]
    },

    chainWebpack: (config) => {
        config.resolve.alias
            .set('@$', resolve('src'));

        const svgRule = config.module.rule('svg');
        svgRule.uses.clear();
        svgRule
            .oneOf('inline')
            .resourceQuery(/inline/)
            .use('vue-svg-icon-loader')
            .loader('vue-svg-icon-loader')
            .end()
            .end()
            .oneOf('external')
            .use('file-loader')
            .loader('file-loader')
            .options({
                name: 'assets/[name].[hash:8].[ext]'
            });
    },

    css: {
        loaderOptions: {
            less: {
                modifyVars: {
                    // less vars，customize ant design theme

                    // 'primary-color': '#F5222D',
                    // 'link-color': '#F5222D',
                    // 'border-radius-base': '4px'
                },
                javascriptEnabled: true
            }
        }
    },

    devServer: {
        // development server port 8000
        port: 8000,
        // If you want to turn on the proxy, please remove the mockjs /src/main.jsL11
        proxy: {
            '/': {
                target: 'http://localhost:9090',
                // target: 'http://120.24.90.60:8001',
                ws: false,
                changeOrigin: true
            }
            // '/auth': {
            //     target: 'http://localhost:9090',
            //     // target: 'http://120.24.90.60:8001',
            //     ws: false,
            //     changeOrigin: true
            // }
        }
    },

    // disable source map in production
    productionSourceMap: false,
    lintOnSave: undefined,
    // babel-loader no-ignore node_modules/*
    transpileDependencies: []
};

module.exports = vueConfig;
