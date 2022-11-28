import networkx as nx
from matplotlib import pyplot as plt
import numpy as np
import os
import pickle
from sklearn.linear_model import LinearRegression


def createGraph(path):
    G = nx.Graph()
    with open(path) as file:
        line = file.readline()
        while (line != ''):
            edge = line.rstrip("\n").split(' ')
            G.add_edge(edge[0], edge[1])
            line = file.readline()
    return G


def plotRunningTimes():
    files = [x[:-5] for x in os.listdir("data/times/")]
    if os.path.isfile("data/graph_sizes.bin"):
        with open("data/graph_sizes.bin", "rb") as graphSizesFile:
            graph_sizes = pickle.load(graphSizesFile)
    else:
       graph_sizes = {}

    sizes = []
    times = []
    error = []
    for f in files:
        if f not in graph_sizes:
            G = createGraph("data/inputs/" + f + ".edges")
            graph_sizes[f] = {"n": len(G.nodes), "m": len(G.edges)}
            with open("data/graph_sizes.bin", "wb") as graphSizesFile:
                pickle.dump(graph_sizes, graphSizesFile)
        sizes.append(graph_sizes[f]["n"] + graph_sizes[f]["m"])
        ftimes = []
        with open("data/times/" + f + ".time", "r") as t:
            line = t.readline()
            while line != '':
                ftimes.append(int(line))
                line = t.readline()

        times.append(np.mean(ftimes))
        error.append(np.std(ftimes) / 2)

    sizes = np.array(sizes)
    times = np.array(times)
    error = np.array(error)

    sizesInput = sizes.reshape(-1, 1)
    timesInput = times
    model = LinearRegression().fit(sizesInput, timesInput)
    r = model.score(sizesInput, timesInput)
    a = model.coef_
    b = model.intercept_
    print(a)

    plt.title("Algorithm running time in function of the size of the graph")
    plt.xlabel('Size of the graph (number of nodes + number of edges)')
    plt.ylabel('Computation time (ns)')
    plt.errorbar(sizes, times, yerr=error,  marker='x', linestyle='',  label="running times")
    plt.tight_layout()
    plt.plot(sizes, a[0] * sizes + b, linewidth=1, color='grey', label='linear regression, R=' + str(round(r, 5)))
    plt.legend(loc='best')
    plt.savefig("images/running-times-huge.png", format="png", bbox_inches='tight')
    
    plt.clf()
    plt.title("Algorithm running time in function of the size of the graph")
    plt.xlabel('Size of the graph (number of nodes + number of edges)')
    plt.ylabel('Computation time (ns)')
    plt.tight_layout()
    plt.errorbar(sizes, times, yerr=error,  marker='x', linestyle='',  label="running times")
    plt.legend(loc='best')
    xlims = [4e7, 8e6, 15e5, 3e5]
    names = ['large', 'medium', 'small', 'tiny']
    for i in range(len(xlims)):
        plt.xlim([-xlims[i] * 0.05, xlims[i]])
        visible_indexes = [x for x in range(len(sizes)) if sizes[x] < xlims[i]]
        visible_times = [times[x] for x in visible_indexes]
        max_visible_time = max(visible_times)
        plt.ylim([- max_visible_time * 0.05, max_visible_time * 1.1])
        plt.savefig("images/running-times-" + names[i] + ".png", format="png", bbox_inches='tight')


plotRunningTimes()
