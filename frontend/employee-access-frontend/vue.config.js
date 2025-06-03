const path = require('path'); // Añade esta línea

module.exports = {
  transpileDependencies: true,
  configureWebpack: {
    resolve: {
      alias: {
        '@': path.resolve(__dirname, 'src/'), // Ahora path está definido
      },
    },
  },
};