const {defineConfig} = require('@vue/cli-service')
module.exports = defineConfig({
    transpileDependencies: true,
    lintOnSave: false,
    devServer: {
        proxy:{
            '/api': {
                target: 'http://localhost:10000',
                pathRewrite:{'^/api':''},
                ws: true,
                changOrigin: true
            }
            // '/api/home': {
            //     target: 'http://localhost:9000',
            //     pathRewrite:{'^/api':''},
            //     ws: true,
            //     changOrigin: true
            // },
            // '/api/user': {
            //     target: 'http://localhost:9100',
            //     pathRewrite:{'^/api':''},
            //     ws: true,
            //     changOrigin: true
            // },
            // '/api/blog': {
            //     target: 'http://localhost:9500',
            //     pathRewrite:{'^/api':''},
            //     ws: true,
            //     changOrigin: true
            // },
            // '/api/image': {
            //     target: 'http://www.kusuri.world',
            //     pathRewrite:{'^/api':''},
            //     ws: true,
            //     changOrigin: true
            // }

        }
    }
})
