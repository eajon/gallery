const debug = process.env.NODE_ENV !== 'production'

module.exports = {
    productionSourceMap: debug,
    lintOnSave: debug,
    baseUrl: debug ? 'webapp' : 'static/webapp',
    outputDir: '../src/main/resources/static/webapp',
    devServer: {
        proxy: {
            '/api': {
                target: 'http://127.0.0.1:8088/',
                changeOrigin: true,
                ws: true,
                pathRewrite: {
                    '^/api': ''
                }
            }
        }
    }
}
