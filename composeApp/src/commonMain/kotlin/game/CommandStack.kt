//package game
//
//import models.GameAction
//
//
//class CommandStack(data: List<GameAction>?) {
//
//    private var stack: MutableList<GameAction> = mutableListOf()
//
//    init {
//        if (data != null) {
//            stack.addAll(data)
//            stack.sortWith { a, b ->
//                return@sortWith a.index - b.index
//            }
//        }
//    }
//
//    fun push(action: GameAction) {
//        stack.add(action)
//    }
//
//    fun pop(): GameAction? = stack.removeLastOrNull()
//
//
//}