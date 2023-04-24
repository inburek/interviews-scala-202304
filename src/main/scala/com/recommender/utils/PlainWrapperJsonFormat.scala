package com.recommender.utils

import spray.json.{JsValue, JsonFormat}

final class PlainWrapperJsonFormat[Target, Underlying : JsonFormat](wrap: Underlying => Target,
                                                                    unwrap: Target => Underlying) extends JsonFormat[Target] {

  def write(obj: Target): JsValue = implicitly[JsonFormat[Underlying]].write(unwrap(obj))
  def read(json: JsValue): Target = wrap(implicitly[JsonFormat[Underlying]].read(json))
}
