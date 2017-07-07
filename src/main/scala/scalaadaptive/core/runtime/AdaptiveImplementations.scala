package scalaadaptive.core.runtime

import scalaadaptive.analytics.AnalyticsSerializer
import scalaadaptive.core.functions.{CombinedFunctionInvoker, FunctionFactory}
import scalaadaptive.core.functions.adaptors.FunctionConfig
import scalaadaptive.core.functions.references.CustomIdentifierValidator

/**
  * Created by pk250187 on 6/29/17.
  */
class AdaptiveImplementations(val identifierValidator: CustomIdentifierValidator,
                              val multiFunctionDefaults: FunctionConfig,
                              val functionFactory: FunctionFactory,
                              val analyticsSerializer: AnalyticsSerializer,
                              val runner: AdaptiveSelector,
                              val persistentRunner: AdaptiveSelector,
                              val functionInvoker: CombinedFunctionInvoker)