const CracoLessPlugin = require('craco-less');

module.exports = {
  plugins: [
    {
      plugin: CracoLessPlugin,
      options: {
        lessLoaderOptions: {
          lessOptions: {
            modifyVars: { '@table-header-bg': '#1890ff', '@table-header-color': '#fff' },
            javascriptEnabled: true,
          },
        },
      },
    },
  ],
};