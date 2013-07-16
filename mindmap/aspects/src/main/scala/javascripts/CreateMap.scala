package javascripts

/*trait CreateMap extends JS with Models with Templates {
	val main = fun { () =>
    val input = document.find(".form input")
    input.addListener("keyup") { e =>
      if (e.keyCode == 13) { // Enter was pressed
        Ajax(new JSLiteral {
          val url = "/"
          val type = "post"
          val data = new JSLiteral { val name = input.value }
          val dataType = "json"
          val success = fun { (map: MindMap) =>
            document.find("ul").appendChild(itemMap(map).root) // TODO use a tree transformation
          }
          val error = fun { (e: Event) =>
            window.alert("Unable to create the event!")
          }
        })
      }
    }
  }
}*/