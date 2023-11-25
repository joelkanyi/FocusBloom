// default-settings
;(function (config) {
if (!!config.output) {
  config.output.chunkFilename = '[name].[contenthash].js'
  config.output.clean = true
}
})(config)

// resources
;(function (config) {
        config.resolve.modules.unshift(
            '/Users/joelkanyi/StudioProjects/FocusBloom/shared/src/commonMain/resources',
'/Users/joelkanyi/StudioProjects/FocusBloom/web/src/jsMain/resources'
        )
})(config)