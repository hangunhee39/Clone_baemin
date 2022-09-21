package hgh.project.baemin_clone.widget.adapter.viewholder

import hgh.project.baemin_clone.databinding.ViewholderEmptyBinding
import hgh.project.baemin_clone.model.Model
import hgh.project.baemin_clone.screen.base.BaseViewModel
import hgh.project.baemin_clone.util.provider.ResourceProvider
import hgh.project.baemin_clone.widget.adapter.listener.AdapterListener

class EmptyViewHolder(
    private val binding: ViewholderEmptyBinding,
    viewModel: BaseViewModel,
    resourceProvider: ResourceProvider
): ModelVIewHolder<Model>(binding, viewModel, resourceProvider) {

    override fun bindViews(model: Model, adapterListener: AdapterListener) {
        TODO("Not yet implemented")
    }

    override fun reset() {
        TODO("Not yet implemented")
    }
}