package com.dimfcompany.aknewsclient.logic.models

import com.dimfcompany.aknewsclient.base.ObjectWithDates
import com.dimfcompany.aknewsclient.base.ObjectWithId
import com.dimfcompany.aknewsclient.base.enums.TypeNewsCategory
import com.dimfcompany.aknewsclient.base.extensions.toObjOrNullGson
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.io.Serializable
import java.lang.reflect.Type
import java.util.*
import kotlin.collections.ArrayList

class ModelNews(
        override var id: Long? = null,
        var event_id: Long? = null,
        var name: String?,
        var author_id: Long?,
        var author_name: String?,
        var author_email: String?,
        var text: String?,
        var category: TypeNewsCategory?,
        var files: ArrayList<ModelFile>? = arrayListOf(),
        var images: ArrayList<ModelFile>? = arrayListOf(),
        override var created: Date?,
        override var updated: Date?,
        override var deleted: Date?
) : Serializable, ObjectWithId, ObjectWithDates
{
    companion object
    {
        fun getDummyNews(): ModelNews
        {
            val str = "{\n" +
                    "        \"id\": 1,\n" +
                    "        \"event_id\": null,\n" +
                    "        \"name\": \"Тест название\",\n" +
                    "        \"author_id\": 1,\n" +
                    "        \"author_name\": \"Тест имя\",\n" +
                    "        \"author_email\": \"Bios90@mail.ru\",\n" +
                    "        \"text\": \"Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.\",\n" +
                    "        \"category\": \"ops\",\n" +
                    "        \"files_ids\": \"7-8-6-5\",\n" +
                    "        \"images_ids\": \"2-1-3-4\",\n" +
                    "        \"created_at\": \"2021-01-09 23:41:15\",\n" +
                    "        \"updated_at\": \"2021-01-09 23:41:15\",\n" +
                    "        \"deleted_at\": null,\n" +
                    "        \"files\": [\n" +
                    "            {\n" +
                    "                \"id\": 5,\n" +
                    "                \"file_name\": \"049236d4d877d1525c7c8c41b4c3f0d12c3ba7d5.png\",\n" +
                    "                \"file_original_name\": \"2021-01-10-02-41-04-546.png\",\n" +
                    "                \"file_mime_type\": \"image/png\",\n" +
                    "                \"file_size\": 286429,\n" +
                    "                \"created_at\": \"2021-01-09 23:41:15\",\n" +
                    "                \"updated_at\": \"2021-01-09 23:41:15\",\n" +
                    "                \"deleted_at\": null,\n" +
                    "                \"url\": \"http://akcsl.test/storage/files/049236d4d877d1525c7c8c41b4c3f0d12c3ba7d5.png\"\n" +
                    "            },\n" +
                    "            {\n" +
                    "                \"id\": 6,\n" +
                    "                \"file_name\": \"049236d4d877d1525c7c8c41b4c3f0d12c3ba7d5.jar\",\n" +
                    "                \"file_original_name\": \"2021-01-10-02-40-42-375\",\n" +
                    "                \"file_mime_type\": \"application/java-archive\",\n" +
                    "                \"file_size\": 149732,\n" +
                    "                \"created_at\": \"2021-01-09 23:41:15\",\n" +
                    "                \"updated_at\": \"2021-01-09 23:41:15\",\n" +
                    "                \"deleted_at\": null,\n" +
                    "                \"url\": \"http://akcsl.test/storage/files/049236d4d877d1525c7c8c41b4c3f0d12c3ba7d5.jar\"\n" +
                    "            },\n" +
                    "            {\n" +
                    "                \"id\": 7,\n" +
                    "                \"file_name\": \"049236d4d877d1525c7c8c41b4c3f0d12c3ba7d5.bin\",\n" +
                    "                \"file_original_name\": \"2021-01-10-02-40-48-406.docx\",\n" +
                    "                \"file_mime_type\": \"application/octet-stream\",\n" +
                    "                \"file_size\": 52926,\n" +
                    "                \"created_at\": \"2021-01-09 23:41:15\",\n" +
                    "                \"updated_at\": \"2021-01-09 23:41:15\",\n" +
                    "                \"deleted_at\": null,\n" +
                    "                \"url\": \"http://akcsl.test/storage/files/049236d4d877d1525c7c8c41b4c3f0d12c3ba7d5.bin\"\n" +
                    "            },\n" +
                    "            {\n" +
                    "                \"id\": 8,\n" +
                    "                \"file_name\": \"049236d4d877d1525c7c8c41b4c3f0d12c3ba7d5.jpeg\",\n" +
                    "                \"file_original_name\": \"2021-01-10-02-40-35-977\",\n" +
                    "                \"file_mime_type\": \"image/jpeg\",\n" +
                    "                \"file_size\": 60005,\n" +
                    "                \"created_at\": \"2021-01-09 23:41:15\",\n" +
                    "                \"updated_at\": \"2021-01-09 23:41:15\",\n" +
                    "                \"deleted_at\": null,\n" +
                    "                \"url\": \"http://akcsl.test/storage/files/049236d4d877d1525c7c8c41b4c3f0d12c3ba7d5.jpeg\"\n" +
                    "            }\n" +
                    "        ],\n" +
                    "        \"images\": [\n" +
                    "            {\n" +
                    "                \"id\": 1,\n" +
                    "                \"file_name\": \"049236d4d877d1525c7c8c41b4c3f0d12c3ba7d5.jpeg\",\n" +
                    "                \"file_original_name\": \"2021-01-10-02-39-58-909.jpg\",\n" +
                    "                \"file_mime_type\": \"image/jpeg\",\n" +
                    "                \"file_size\": 5375795,\n" +
                    "                \"created_at\": \"2021-01-09 23:41:14\",\n" +
                    "                \"updated_at\": \"2021-01-09 23:41:14\",\n" +
                    "                \"deleted_at\": null,\n" +
                    "                \"url\": \"http://akcsl.test/storage/files/049236d4d877d1525c7c8c41b4c3f0d12c3ba7d5.jpeg\"\n" +
                    "            },\n" +
                    "            {\n" +
                    "                \"id\": 2,\n" +
                    "                \"file_name\": \"049236d4d877d1525c7c8c41b4c3f0d12c3ba7d5.jpeg\",\n" +
                    "                \"file_original_name\": \"2021-01-10-02-40-03-272.jpg\",\n" +
                    "                \"file_mime_type\": \"image/jpeg\",\n" +
                    "                \"file_size\": 4878782,\n" +
                    "                \"created_at\": \"2021-01-09 23:41:14\",\n" +
                    "                \"updated_at\": \"2021-01-09 23:41:14\",\n" +
                    "                \"deleted_at\": null,\n" +
                    "                \"url\": \"http://akcsl.test/storage/files/049236d4d877d1525c7c8c41b4c3f0d12c3ba7d5.jpeg\"\n" +
                    "            },\n" +
                    "            {\n" +
                    "                \"id\": 3,\n" +
                    "                \"file_name\": \"049236d4d877d1525c7c8c41b4c3f0d12c3ba7d5.jpeg\",\n" +
                    "                \"file_original_name\": \"2021-01-10-02-40-24-916.jpg\",\n" +
                    "                \"file_mime_type\": \"image/jpeg\",\n" +
                    "                \"file_size\": 6304572,\n" +
                    "                \"created_at\": \"2021-01-09 23:41:14\",\n" +
                    "                \"updated_at\": \"2021-01-09 23:41:14\",\n" +
                    "                \"deleted_at\": null,\n" +
                    "                \"url\": \"http://akcsl.test/storage/files/049236d4d877d1525c7c8c41b4c3f0d12c3ba7d5.jpeg\"\n" +
                    "            },\n" +
                    "            {\n" +
                    "                \"id\": 4,\n" +
                    "                \"file_name\": \"049236d4d877d1525c7c8c41b4c3f0d12c3ba7d5.jpeg\",\n" +
                    "                \"file_original_name\": \"2021-01-10-02-40-17-383.jpg\",\n" +
                    "                \"file_mime_type\": \"image/jpeg\",\n" +
                    "                \"file_size\": 6497727,\n" +
                    "                \"created_at\": \"2021-01-09 23:41:14\",\n" +
                    "                \"updated_at\": \"2021-01-09 23:41:14\",\n" +
                    "                \"deleted_at\": null,\n" +
                    "                \"url\": \"http://akcsl.test/storage/files/049236d4d877d1525c7c8c41b4c3f0d12c3ba7d5.jpeg\"\n" +
                    "            }\n" +
                    "        ]\n" +
                    "    }"

            return str.toObjOrNullGson(ModelNews::class.java)!!
        }
    }
}

fun List<ModelNews>.getNewsOfType(type: TypeNewsCategory): ArrayList<ModelNews>
{
    val items = this.filter({ it.category == type }).toCollection(ArrayList())
    return items
}

fun List<ModelNews>.toMap(): Map<TypeNewsCategory?, List<ModelNews>>
{
    val items = this.groupBy({ it.category })
    return items
}



