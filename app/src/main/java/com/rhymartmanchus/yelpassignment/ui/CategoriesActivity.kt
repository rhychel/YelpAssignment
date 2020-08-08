package com.rhymartmanchus.yelpassignment.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import com.rhymartmanchus.yelpassignment.InstanceProvider
import com.rhymartmanchus.yelpassignment.databinding.ActivityCategoriesBinding
import com.rhymartmanchus.yelpassignment.domain.models.Category
import com.rhymartmanchus.yelpassignment.domain.models.SubcategoryAttributedCategory
import com.rhymartmanchus.yelpassignment.ui.adapters.CategoriesAdapter
import com.rhymartmanchus.yelpassignment.ui.fragments.CategoriesContract
import com.rhymartmanchus.yelpassignment.ui.fragments.CategoriesFragment
import com.rhymartmanchus.yelpassignment.ui.fragments.CategoriesPresenter

class CategoriesActivity : AppCompatActivity(), CategoriesAdapter.OnCategorySelectedListener {

    private val binder: ActivityCategoriesBinding by lazy {
        ActivityCategoriesBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binder.root)

        setSupportActionBar(binder.tbActionbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Categories"

        val presenter: CategoriesContract.Presenter = providePresenter()
        val categoriesFragment = CategoriesFragment(
            presenter,
            this,
            true
        )
        presenter.takeView(categoriesFragment)

        supportFragmentManager.beginTransaction()
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .replace(binder.flCategories.id, categoriesFragment, "level1")
            .addToBackStack("level1")
            .commit()

    }

    override fun onBackPressed() {
        if(supportFragmentManager.backStackEntryCount == 1)
            finish()
        else
            super.onBackPressed()
    }

    private fun providePresenter(): CategoriesContract.Presenter = CategoriesPresenter(
        InstanceProvider.appCoroutinesDispatcher,
        InstanceProvider.getSubcategoryAttributedCategoriesUseCase,
        InstanceProvider.getSubcategoryAttributedCategoriesByAliasUseCase
    )

    private fun openSubCategories(subcategoryAttributedCategory: SubcategoryAttributedCategory) {
        val presenter: CategoriesContract.Presenter = providePresenter()
        val categoriesFragment = CategoriesFragment(
            presenter,
            this
        )
        categoriesFragment.takeParentAlias(subcategoryAttributedCategory.alias)
        presenter.takeView(categoriesFragment)

        supportFragmentManager.beginTransaction()
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .replace(binder.flCategories.id, categoriesFragment, subcategoryAttributedCategory.alias)
            .addToBackStack(subcategoryAttributedCategory.alias)
            .commit()
    }

    override fun onSelected(subcategoryAttributedCategory: SubcategoryAttributedCategory) {
        if(subcategoryAttributedCategory.subcategories.isNotEmpty()) {
            openSubCategories(subcategoryAttributedCategory)
        }
        else {
            val data = Intent()
            data.putExtra("category", Category(
                subcategoryAttributedCategory.alias,
                subcategoryAttributedCategory.title,
                emptyList()
            ))
            setResult(200, data)
            finish()
        }
    }

}