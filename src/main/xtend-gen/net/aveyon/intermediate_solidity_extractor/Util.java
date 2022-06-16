package net.aveyon.intermediate_solidity_extractor;

import com.google.common.base.Objects;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.aveyon.intermediate_solidity.Constructor;
import net.aveyon.intermediate_solidity.Field;
import net.aveyon.intermediate_solidity.Function;
import net.aveyon.intermediate_solidity.FunctionParameter;
import net.aveyon.intermediate_solidity.LocalField;
import net.aveyon.intermediate_solidity.Modifier;
import net.aveyon.intermediate_solidity.Node;
import net.aveyon.intermediate_solidity.SmartContract;
import net.aveyon.intermediate_solidity.Visibility;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.Conversions;

@SuppressWarnings("all")
public class Util {
  public static String printFunctionKeyWords(final Function function) {
    StringConcatenation _builder = new StringConcatenation();
    String _printVisibility = Util.printVisibility(function.getVisibility());
    _builder.append(_printVisibility);
    StringConcatenation _builder_1 = new StringConcatenation();
    {
      boolean _isPure = function.isPure();
      if (_isPure) {
        _builder_1.append(" pure");
      }
    }
    String _plus = (_builder.toString() + _builder_1);
    StringConcatenation _builder_2 = new StringConcatenation();
    {
      boolean _doesOverride = function.getDoesOverride();
      if (_doesOverride) {
        _builder_2.append(" override");
      }
    }
    String _plus_1 = (_plus + _builder_2);
    StringConcatenation _builder_3 = new StringConcatenation();
    {
      boolean _isVirtual = function.isVirtual();
      if (_isVirtual) {
        _builder_3.append(" virtual");
      }
    }
    String _plus_2 = (_plus_1 + _builder_3);
    StringConcatenation _builder_4 = new StringConcatenation();
    {
      boolean _payable = function.getPayable();
      if (_payable) {
        _builder_4.append(" payable");
      }
    }
    return (_plus_2 + _builder_4);
  }
  
  public static String printFunctionParameterKeyWords(final FunctionParameter param) {
    StringConcatenation _builder = new StringConcatenation();
    {
      boolean _payable = param.getPayable();
      if (_payable) {
        _builder.append(" payable");
      }
    }
    StringConcatenation _builder_1 = new StringConcatenation();
    {
      if (((param.getDataLocation() != null) && (!Util.isPrimitive(param.getType())))) {
        _builder_1.append(" ");
        String _lowerCase = param.getDataLocation().name().toLowerCase();
        _builder_1.append(_lowerCase);
      }
    }
    return (_builder.toString() + _builder_1);
  }
  
  public static String printModifierKeyWords(final Modifier modifier) {
    StringConcatenation _builder = new StringConcatenation();
    {
      boolean _doesOverride = modifier.getDoesOverride();
      if (_doesOverride) {
        _builder.append(" override");
      }
    }
    StringConcatenation _builder_1 = new StringConcatenation();
    {
      boolean _isVirtual = modifier.isVirtual();
      if (_isVirtual) {
        _builder_1.append(" virtual");
      }
    }
    return (_builder.toString() + _builder_1);
  }
  
  public static String printConstructorKeyWords(final Constructor ctor) {
    final String v = Util.printVisibility(ctor.getVisibility());
    StringConcatenation _builder = new StringConcatenation();
    {
      boolean _payable = ctor.getPayable();
      if (_payable) {
        _builder.append(" payable");
      }
    }
    _builder.append(" ");
    {
      String _lowerCase = Visibility.PUBLIC.name().toLowerCase();
      boolean _notEquals = (!Objects.equal(v, _lowerCase));
      if (_notEquals) {
        _builder.append(v);
      }
    }
    return _builder.toString();
  }
  
  public static String printFieldKeyWords(final Field field) {
    StringConcatenation _builder = new StringConcatenation();
    String _printLocalFieldKeyWords = Util.printLocalFieldKeyWords(field);
    _builder.append(_printLocalFieldKeyWords);
    _builder.append(" ");
    String _printVisibility = Util.printVisibility(field.getVisibility());
    _builder.append(_printVisibility);
    return _builder.toString();
  }
  
  public static String printLocalFieldKeyWords(final LocalField field) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append(field.type);
    {
      boolean _payable = field.getPayable();
      if (_payable) {
        _builder.append(" payable");
      }
    }
    return _builder.toString();
  }
  
  public static String printVisibility(final Visibility visibility) {
    StringConcatenation _builder = new StringConcatenation();
    {
      if ((visibility != null)) {
        String _lowerCase = visibility.name().toLowerCase();
        _builder.append(_lowerCase);
      } else {
        _builder.append("public");
      }
    }
    return _builder.toString();
  }
  
  public static String printReturnValues(final List<String> returnValues) {
    if (((returnValues == null) || (returnValues.size() == 0))) {
      return "";
    }
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("returns (");
    {
      boolean _hasElements = false;
      for(final String r : returnValues) {
        if (!_hasElements) {
          _hasElements = true;
        } else {
          _builder.appendImmediate(", ", "");
        }
        _builder.append(r);
        {
          boolean _isPrimitive = Util.isPrimitive(r);
          boolean _not = (!_isPrimitive);
          if (_not) {
            _builder.append(" memory");
          }
        }
      }
    }
    _builder.append(")");
    return _builder.toString();
  }
  
  public static String printExtension(final SmartContract contract) {
    if (((((Object[])Conversions.unwrapArray(contract.getExtends(), Object.class)).length == 0) && (((Object[])Conversions.unwrapArray(contract.getImplements(), Object.class)).length == 0))) {
      return "";
    }
    final List<Node> extensions = Stream.<Node>concat(contract.getExtends().stream(), contract.getImplements().stream()).collect(Collectors.<Node>toList());
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("is ");
    {
      boolean _hasElements = false;
      for(final Node e : extensions) {
        if (!_hasElements) {
          _hasElements = true;
        } else {
          _builder.appendImmediate(", ", "");
        }
        String _name = e.getName();
        _builder.append(_name);
      }
    }
    return _builder.toString();
  }
  
  public static boolean isPrimitive(final String type) {
    return (((((type.startsWith("uint") || type.startsWith("int")) || type.startsWith("float")) || Objects.equal(type, "bool")) || Objects.equal(type, "address")) || (type.contains("byte") && (!Objects.equal(type, "bytes"))));
  }
}
