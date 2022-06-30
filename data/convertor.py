import networkx as nx
import os
import pickle

def convert(path, fileName, extension='.txt', separator=' ', start=0):
    G = nx.Graph()
    with open("data/downloaded/" + path + fileName + extension, 'r') as fromFile:
        for i in range(start + 1):
            line = fromFile.readline()
        while (line != ''):
            edge = line.rstrip("\n").split(separator)
            G.add_edge(edge[0], edge[1])
            line = fromFile.readline()

    with open("data/inputs/" + fileName + ".edges", 'w+') as toFile:
        toFile.truncate(0)
        for edge in G.edges:
            toFile.write(edge[0] + " " + edge[1] + "\n")
            toFile.write(edge[1] + " " + edge[0] + "\n")

    if (os.path.isfile("data/graph_sizes.bin")):
        with open("data/graph_sizes.bin", "rb") as graphSizesFile:
            graph_sizes = pickle.load(graphSizesFile)
    else:
        graph_sizes = {}
    graph_sizes[fileName] = {"n": len(G.nodes), "m": len(G.edges)}

    with open("data/graph_sizes.bin", "wb") as graphSizesFile:
        pickle.dump(graph_sizes, graphSizesFile)
        

# convert("com-amazon/", "com-amazon.ungraph", ".txt", '	', 4)

# convert("com-dblp/", "com-dblp.ungraph", ".txt", '	', 4)

# convert("com-friendster/", "com-friendster.ungraph", ".txt", '     ', 4)

# convert("com-livejournal/", "com-lj.ungraph", ".txt", '	', 4)

# convert("com-orkut/", "com-orkut.ungraph", ".txt", '	', 4)

# convert("com-youtube/", "com-youtube", ".txt", '	', 4)

# convert("ego-facebook/", "0", ".edges", ' ', 0)
# convert("ego-facebook/", "107", ".edges", ' ', 0)
# convert("ego-facebook/", "348", ".edges", ' ', 0)
# convert("ego-facebook/", "414", ".edges", ' ', 0)
# convert("ego-facebook/", "686", ".edges", ' ', 0)
# convert("ego-facebook/", "1684", ".edges", ' ', 0)
# convert("ego-facebook/", "1912", ".edges", ' ', 0)
# convert("ego-facebook/", "3437", ".edges", ' ', 0)
# convert("ego-facebook/", "3980", ".edges", ' ', 0)

# convert("email-enron/", "Email-Enron", ".txt", '	', 4)

# convert("email-eu-core/", "email-Eu-core", ".txt", '	', 0)

# convert("feather-deezer-social/", "deezer_europe_edges", ".csv", ',', 1)

# convert("feather-lastfm-social/", "lastfm_asia_edges", ".csv", ',', 1)

# convert("gemsec-deezer/", "HR_edges", ".csv", ',', 1)
# convert("gemsec-deezer/", "HU_edges", ".csv", ',', 1)
# convert("gemsec-deezer/", "RO_edges", ".csv", ',', 1)

# convert("gemsec-facebook/", "artist_edges", ".csv", ',', 1)
# convert("gemsec-facebook/", "athletes_edges", ".csv", ',', 1)
# convert("gemsec-facebook/", "company_edges", ".csv", ',', 1)
# convert("gemsec-facebook/", "government_edges", ".csv", ',', 1)
# convert("gemsec-facebook/", "new_sites_edges", ".csv", ',', 1)
# convert("gemsec-facebook/", "politician_edges", ".csv", ',', 1)
# convert("gemsec-facebook/", "public_figure_edges", ".csv", ',', 1)
# convert("gemsec-facebook/", "tvshow_edges", ".csv", ',', 1)

# convert("musae-facebook/", "musae_facebook_edges", ".csv", ',', 1)

# convert("musae-twitch/", "musae_DE_edges", ".csv", ',', 1)
# convert("musae-twitch/", "musae_ENGB_edges", ".csv", ',', 1)
# convert("musae-twitch/", "musae_ES_edges", ".csv", ',', 1)
# convert("musae-twitch/", "musae_FR_edges", ".csv", ',', 1)
# convert("musae-twitch/", "musae_PTBR_edges", ".csv", ',', 1)
# convert("musae-twitch/", "musae_RU_edges", ".csv", ',', 1)

# convert("roadNet-CA/", "roadNet-CA", ".txt", '	', 4)

# convert("roadNet-PA/", "roadNet-PA", ".txt", '	', 4)

# convert("roadNet-TX/", "roadNet-TX", ".txt", '	', 4)

# convert("twitch-gamers/", "large_twitch_edges", ".csv", ',', 1)

# convert("wiki-topcats/", "wiki-topcats", ".txt", ' ', 0)
