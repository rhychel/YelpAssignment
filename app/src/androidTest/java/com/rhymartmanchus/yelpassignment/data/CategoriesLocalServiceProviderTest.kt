package com.rhymartmanchus.yelpassignment.data

import androidx.test.platform.app.InstrumentationRegistry
import com.rhymartmanchus.yelpassignment.data.db.CategoryAssocDB
import com.rhymartmanchus.yelpassignment.data.db.CategoryDB
import com.rhymartmanchus.yelpassignment.data.db.YelpDatabase
import com.rhymartmanchus.yelpassignment.domain.models.Category
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test

import org.junit.Assert.*

class CategoriesLocalServiceProviderTest {

    private lateinit var categoriesLocalService: CategoriesLocalService

    private val db: YelpDatabase by lazy {
        YelpDatabase.getInstance(InstrumentationRegistry.getInstrumentation().targetContext)
    }

    private val categories: List<Category> = listOf(
        Category(
            "a-1",
            "A1",
            listOf()
        ),
        Category(
            "a-2",
            "A2",
            listOf(
                "a-1"
            )
        ),
        Category(
            "b-1",
            "B1",
            listOf(
                "c-1",
                "b-1"
            )
        ),
        Category(
            "b-2",
            "b2",
            listOf(
                "c-2"
            )
        ),
        Category(
            "c-1",
            "C1",
            listOf()
        ),
        Category(
            "c-2",
            "C2",
            listOf(
                "b-2"
            )
        )
    )

    @Before
    fun setUp() {

        categoriesLocalService = CategoriesLocalServiceProvider(
                db.categoriesDao()
        )
    }

    @After
    fun tearDown() {
        db.clearAllTables()
        YelpDatabase.destroyInstance()
    }

    @Test
    fun getCategories() = runBlocking {
        categories.forEach {
            db.categoriesDao().saveCategory(it.toDB())
            it.parentAliases.forEach { parent ->
                db.categoriesDao().saveCategoryAssoc(
                    CategoryAssocDB(parent, it.alias)
                )
            }
        }
        val categories = categoriesLocalService.getSubcategoryAttributedCategories(3, 0)

        assertEquals(3, categories.size)

        categories.forEach {
            println("Category: ${it.alias}, Parents: ${it.subcategories.size}")
        }
    }

    @Test
    fun saveCategories() = runBlocking {
        categoriesLocalService.saveCategories(categories)

        assertEquals(6, db.categoriesDao().getSubcategoryAttributedCategories(6, 0).size)
    }
}