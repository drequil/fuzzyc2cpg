package io.shiftleft.fuzzyc2cpg

import io.shiftleft.fuzzyc2cpg.ast.AstNode
import io.shiftleft.proto.cpg.Cpg
import io.shiftleft.proto.cpg.Cpg.CpgStruct.Node.Property
import io.shiftleft.proto.cpg.Cpg.PropertyValue

object Utils {

  def newStringProperty(name: Cpg.NodePropertyName, value: String) =
    Property.newBuilder
      .setName(name)
      .setValue(PropertyValue.newBuilder.setStringValue(value).build)

  def children(node: AstNode) =
    (0 to node.getChildCount).map(node.getChild)
      .filterNot(_ == null)
      .toList


}
