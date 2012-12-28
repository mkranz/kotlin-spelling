import java.io.File
import java.util.ArrayList
import java.util.Dictionary
import java.util.HashMap
import java.util.HashSet
import java.util.Arrays
import extensions.or

// Peter Norvig's "How to Write a Spelling Corrector"
// http://norvig.com/spell-correct.html
// Kotlin implementation by Mark Kranz

object Model {
    var NWORDS: Map<String, Int> = HashMap<String,Int>()

    fun load(filename: String) {
        var file = File("big.txt")
        NWORDS = file
                .readLines()
                .flatMap{line -> line.split(" ")
                .filter{word -> word.matches("[a-z]+")}
                .map{word -> word.toLowerCase()} }
                .groupBy { word -> word }
                .mapValues { kv -> kv.value.size  }
    }

    fun known(words: Iterable<String>): Iterable<String> {
        return words.filter{word -> NWORDS.containsKey(word)}
    }

    fun max(a : String, b : String) : String {
        return if(NWORDS[a] ?: 0 > NWORDS[b] ?: 0) a else b
    }

    fun correct(word: String): String {
        var candidates = known(Arrays.asList(word)).or(known(edits1(word))).or(Arrays.asList(word))
        return candidates.reduce { (a,b) -> max(a,b) }
    }
}

fun edits1(word: String) : Set<String> {
    // some thoughts on cleaning up
    // 1) use an anonymous class rather than 'pair'
    // 2) add extension method for ".filter{p -> p.second.length > 0}" (essentially means non empty)
    var alphabet = 'a'..'z'
    var splits = (0..word.size).map{i -> Pair(word.substring(0,i),word.substring(i))}
    var deletes = splits.filter{p -> p.second.length > 0}.map{p -> p.first + p.second.substring(1)}
    var transposes =  splits.filter{p -> p.second.length > 1}.map{p -> p.first+p.second[1] + p.second[0]+p.second.substring(2)}
    var replaces = alphabet.flatMap{a -> splits.filter{p -> p.second.length > 0}.map{p -> p.first + a + p.second.substring(1)}}
    var inserts = alphabet.flatMap{a -> splits.map{p -> p.first + a + p.second}}

    var result = HashSet<String>()
    result.addAll(deletes)
    result.addAll(transposes)
    result.addAll(replaces)
    result.addAll(inserts)

    return result;
}


fun main(args: Array<String>): Unit {
    Model.load("big.txt")

    println("poteto")
    println(Model.correct("poteto"))
}