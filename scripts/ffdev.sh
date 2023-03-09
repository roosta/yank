# remove GTK_DPI_SCALE if needed, its for my 1080 debug monitor
env GDK_DPI_SCALE=0.5 /usr/lib/firefox-developer-edition/firefox "$@"
