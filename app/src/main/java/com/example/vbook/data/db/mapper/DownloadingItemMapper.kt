import com.example.vbook.common.model.DownloadingItem
import com.example.vbook.data.db.model.DownloadingItemEntity

fun DownloadingItem.toData() = DownloadingItemEntity(
    mediaOnlineUri, downloadId, bookUrl
)

fun List<DownloadingItem>.toData() = this.map { it.toData() }

fun DownloadingItemEntity.toDomain() = DownloadingItem(
    mediaUri, downloadId, bookUrl
)

fun List<DownloadingItemEntity>.toDomain() = this.map { it.toDomain() }