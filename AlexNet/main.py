import  os
import pandas as pd
import torch


def getFile(filePath,fileName):
    with open(filePath+fileName, encoding='ISO-8859-1') as file_obj:
        contents = file_obj.read()
        res = contents.split("\n\n")[0]
        return res

def getPostCsv(filePath,isPost):
    contents = []
    sizes = []
    filePaths = []
    num = 0
    if os.path.exists(filePath):
        files = os.listdir(filePath)
    total = len(files)
    for file in files:
        content = getFile(filePath,file)
        if isPost:
            content = dataAnayls(content)
        if content != None:
            file_stat = os.stat(filePath + file)
            size = file_stat.st_size / (1024)
            contents.append(content)
            sizes.append(size)
            filePaths.append(filePath)
            num += 1
        print('The total is %d,mow is %d' % (total, num))

    dataframe = pd.DataFrame({'head': contents, 'file_name': file, 'file_size(kb)' : size})
    return dataframe



def getCsv(filePath):
    contents = []
    sizes = []
    filePaths = []
    num = 0
    if os.path.exists(filePath):
        files = os.listdir(filePath)
    total = len(files)
    for file in files:
        content = getFile(filePath,file)
        file_stat = os.stat(filePath + file)
        size = file_stat.st_size / (1024)
        contents.append(content)
        sizes.append(size)
        filePaths.append(file)
        num += 1
        print('The total is %d,mow is %d' % (total, num))

    dataframe = pd.DataFrame({'head': contents, 'file_name': filePaths, 'file_size(kb)' : sizes})
    return dataframe




def dataAnayls(contents):
    res = contents.split("\n")[0]
    if not res.startswith("PUT"):
        return contents






if __name__ == "__main__":
    filepath = "C:\\Users\\Administrator\\Desktop\\数据\\search-20220705111924034\\"
    # filepath = "C:\\Users\\Administrator\\Desktop\\数据\\search-20220705110456832\\"
    # fileName = "00a3c420-fa61-11ec-c000-c56846a85b9b-140012221880064-265011.http"
    # contents = getFile(filepath,fileName)
    # print("sucess")
    isPost = True
    dataframe = getCsv(filepath)
    # dataframe = getPostCsv(filepath,isPost)
    dataframe.to_csv("C:\\Users\\Administrator\\Desktop\\数据\\search-20220705111924034-test.csv", index=False, sep=',')
#