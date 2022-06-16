package net.aveyon.intermediate_solidity_extractor

import net.aveyon.intermediate_solidity.Visibility
import java.util.List
import net.aveyon.intermediate_solidity.Function
import net.aveyon.intermediate_solidity.Field
import net.aveyon.intermediate_solidity.LocalField
import net.aveyon.intermediate_solidity.Modifier
import net.aveyon.intermediate_solidity.SmartContract
import net.aveyon.intermediate_solidity.Node
import java.util.stream.Stream
import java.util.stream.Collectors
import net.aveyon.intermediate_solidity.Constructor
import net.aveyon.intermediate_solidity.FunctionParameter

class Util {

	def static printFunctionKeyWords(Function function) {
		return '''
		«printVisibility(function.visibility)»''' 
		+ '''«IF function.pure» pure«ENDIF»''' 
		+ '''«IF function.doesOverride» override«ENDIF»''' 
		+ '''«IF function.virtual» virtual«ENDIF»''' 
		+ '''«IF function.payable» payable«ENDIF»'''
	}
	
	def static printFunctionParameterKeyWords(FunctionParameter param) {
		return
		'''«IF param.payable» payable«ENDIF»''' + 
		 '''«IF param.dataLocation !== null && !param.type.isPrimitive» «param.dataLocation.name.toLowerCase»«ENDIF»'''
	}

	def static printModifierKeyWords(Modifier modifier) {
		return '''
		«IF modifier.doesOverride» override«ENDIF»''' + '''
		«IF modifier.virtual» virtual«ENDIF»'''
	}
	
	def static printConstructorKeyWords(Constructor ctor) {
		val v = printVisibility(ctor.visibility)
		// visibility 'public' can be omitted
		return '''«IF ctor.payable» payable«ENDIF» «IF v != Visibility.PUBLIC.name.toLowerCase»«v»«ENDIF»'''
	}

	def static printFieldKeyWords(Field field) {
		return '''«printLocalFieldKeyWords(field)» «printVisibility(field.visibility)»'''
	}

	def static printLocalFieldKeyWords(LocalField field) {
		return '''«field.type»«IF field.payable» payable«ENDIF»'''
	}

	def static printVisibility(Visibility visibility) {
		return '''«IF visibility !== null»«visibility.name.toLowerCase()»«ELSE»public«ENDIF»'''
	}

	def static printReturnValues(List<String> returnValues) {
		if (returnValues === null || returnValues.size() == 0) {
			return ""
		}

		return '''returns («FOR r : returnValues SEPARATOR ", "»«r»«IF !r.isPrimitive» memory«ENDIF»«ENDFOR»)'''
	}
	
	def static printExtension(SmartContract contract) {
		if (contract.extends.length == 0 && contract.implements.length == 0)
			return ""
			
		val List<Node> extensions = Stream.concat(contract.extends.stream(), contract.implements.stream()).collect(Collectors.toList())
		
		return '''is «FOR e: extensions SEPARATOR ", "»«e.name»«ENDFOR»'''
	}
	
	def static boolean isPrimitive(String type) {
		return type.startsWith("uint") || type.startsWith("int") || type.startsWith("float") || type == "bool" || type == "address" || (type.contains("byte") && type != "bytes")
	}
}
