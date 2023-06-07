package com.example.szacunki.features.estimation.presentation.model

import com.example.szacunki.features.estimation.domain.model.EstimationDomain
import com.example.szacunki.features.estimation.domain.model.TreeDomain
import com.example.szacunki.features.estimation.domain.model.TreeQualityClassesDomain
import com.example.szacunki.features.estimation.domain.model.TreeRowDomain
import java.util.*

data class EstimationDisplayable(
    val trees: List<TreeDisplayable> = baseNameList.map { TreeDisplayable(name = it) },
    val sectionNumber: String,
    val memo: String = "",
    val date: Date = Date(),
    val id: UUID = UUID.randomUUID()
) {
    constructor(estimationDomain: EstimationDomain) : this(
        trees = estimationDomain.trees.map { TreeDisplayable(it) },
        sectionNumber = estimationDomain.sectionNumber,
        memo = estimationDomain.memo,
        date = estimationDomain.date,
        id = estimationDomain.id
    )

    fun toEstimationDomain() = EstimationDomain(
        trees = trees.map { it.toTreeDomain() },
        sectionNumber = sectionNumber,
        memo = memo,
        date = date,
        id = id
    )
}

data class TreeDisplayable(
    val name: String,
    val treeRows: List<TreeRowDisplayable> = baseDiameterList.map { TreeRowDisplayable(diameter = it) }
) {
    constructor(treDomain: TreeDomain) : this(
        name = treDomain.name,
        treeRows = treDomain.treeRows.map { TreeRowDisplayable(it) }
    )

    fun toTreeDomain() = TreeDomain(name = name, treeRows = treeRows.map { it.toTreeRowDomain() })
}

data class TreeRowDisplayable(
    val diameter: String,
    val treeQualityClasses: TreeQualityClassesDisplayable = TreeQualityClassesDisplayable(),
    val height: Int = 0,
) {
    constructor(treeRowDomain: TreeRowDomain) : this(
        diameter = treeRowDomain.diameter,
        treeQualityClasses = TreeQualityClassesDisplayable(treeRowDomain.treeQualityClasses),
        height = treeRowDomain.height
    )

    fun toTreeRowDomain() = TreeRowDomain(
        diameter = diameter,
        treeQualityClasses = treeQualityClasses.toTreeQualityClassesDomain(),
        height = height
    )
}

data class TreeQualityClassesDisplayable(
    val class1: Int = 0,
    val class2: Int = 0,
    val class3: Int = 0,
    val classA: Int = 0,
    val classB: Int = 0,
    val classC: Int = 0
) {
    constructor(treeQualityClasses: TreeQualityClassesDomain) : this(
        class1 = treeQualityClasses.class1,
        class2 = treeQualityClasses.class2,
        class3 = treeQualityClasses.class3,
        classA = treeQualityClasses.classA,
        classB = treeQualityClasses.classB,
        classC = treeQualityClasses.classC
    )

    fun toTreeQualityClassesDomain() = TreeQualityClassesDomain(
        class1 = class1,
        class2 = class2,
        class3 = class3,
        classA = classA,
        classB = classB,
        classC = classC
    )

}

val baseNameList = listOf(
    "Dąb Szypułkowy",
    "Dąb Czerwony",
    "Topola Biała",
    "Topola Osika",
    "Buk",
    "Lipa",
    "Brzoza",
    "Czeremcha",
    "Grab",
    "Olszyna",
    "Sosna Zwyczajna",
    "Jodła",
    "Świerk",
    "Modrzew",
    "Cis"
)
val baseDiameterList = listOf(
    "7-8.9",
    "9-10.9",
    "11-12.9",
    "13-14.5",
    "15-16.9",
    "17-18.9",
    "19-20.9",
    "21-22.9",
    "21-22.9",
    "23-24.9",
    "25-26.9",
    "27-30.9",
    "31-34.9",
    "35-38.9",
    "39-42.9",
    "43-46.9",
    "47-50.9",
    "51-54.9",
    "55-58.9",
    "59-62.9",
    "63-66.9",
    "67-70.9",
    "71-74.9",
    "75-78.9",
    "79-82.9",
    "83-86.9",
    "87-90.9"
)


