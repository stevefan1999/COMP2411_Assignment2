package hk.edu.polyu.comp2411.assignment.entity.enum

enum class Gender(var s: String) {
    MALE("Male"),
    FEMALE("Female");

    override fun toString() = s
}