package com.dimfcompany.aknewsclient.logic.utils

import com.dimfcompany.aknewsclient.base.BaseVm
import com.dimfcompany.aknewsclient.base.extensions.openAnyFile
import com.dimfcompany.aknewsclient.logic.models.ModelFile
import com.dimfcompany.aknewsclient.logic.utils.builders.BuilderDownloader
import com.dimfcompany.aknewsclient.logic.utils.files.FileManager
import com.dimfcompany.aknewsclient.logic.utils.files.MyFileItem

class BaseVmHelper(val base_vm: BaseVm)
{
    fun checkAndOpenFile(item: ModelFile)
    {
        if (item.file_original_name != null && FileManager.checkIfFileExists(item.file_original_name!!))
        {
            val file = FileManager.getPdfFile(item.file_original_name!!)
            val my_file_item = MyFileItem.createFromFile(file)
            openAnyFile(my_file_item.getUriForShare())
        }
        else if (item.file_name != null && FileManager.checkIfFileExists(item.file_name!!))
        {
            val file = FileManager.getPdfFile(item.file_original_name!!)
            val my_file_item = MyFileItem.createFromFile(file)
            openAnyFile(my_file_item.getUriForShare())
        }
        else if (item.url != null)
        {
            val name = item.getNameForDownloading()
            val file = FileManager.createPdfFile(name)

            BuilderDownloader()
                    .setUrl(item.url!!)
                    .setFileDestination(file)
                    .setBaseVm(base_vm)
                    .setActionSuccess(
                        {
                            openAnyFile(it.getUriForShare())
                        })
                    .run()
        }
    }
}