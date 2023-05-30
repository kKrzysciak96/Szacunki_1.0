package com.example.szacunki.features.estimation.domain.model

import java.util.*

data class EstimationDomain(
    val trees: List<TreeDomain> = baseNameList.map { TreeDomain(name = it) },
    val sectionNumber: String = "",
    val memo: String = "",
    val date: Date = Date(),
    val id: Int = 0
)

data class TreeDomain(
    val name: String,
    val treeRows: List<TreeRowDomain> = baseDiameterList.map { TreeRowDomain(diameter = it) }
)

data class TreeRowDomain(
    val diameter: String,
    val treeQualityClasses: TreeQualityClassesDomain = TreeQualityClassesDomain(),
    val height: Int = 0,
)

data class TreeQualityClassesDomain(
    val class1: Int = 0,
    val class2: Int = 0,
    val class3: Int = 0,
    val classA: Int = 0,
    val classB: Int = 0,
    val classC: Int = 0,
)

val baseNameList = listOf(
    "Dąb",
    "Sosna",
    "Jodła",
    "Buk",
    "Świerk",
    "Lipa",
    "Modrzew"
)
val baseDiameterList = listOf(
    "7-8.9",
    "10.9",
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


