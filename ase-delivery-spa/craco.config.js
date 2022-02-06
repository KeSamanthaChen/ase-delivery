const CracoLessPlugin = require('craco-less');

module.exports = {
  plugins: [
    {
      plugin: CracoLessPlugin,
      options: {
        lessLoaderOptions: {
          lessOptions: {
            modifyVars: { '@primary-color': '#A891CB',
                          '@body-background': '#f6f4f7',
                          '@layout-header-background': '@body-background',
                          '@layout-header-padding': '0px',
                          '@layout-body-background': '#ffffff',
                          '@layout-header-color': '#333333',
                          '@btn-border-radius-base': '6px',
                          },
            javascriptEnabled: true,
          },
        },
      },
    },
  ],
};